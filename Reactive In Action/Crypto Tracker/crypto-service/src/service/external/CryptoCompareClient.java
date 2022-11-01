package service.external;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import service.external.utils.MessageUnpacker;

class CryptoCompareClient {
    private static final Logger logger = LoggerFactory.getLogger("external-trading-service");

    static Flux<Map<String, Object>> connect(Flux<String> input, Collection<MessageUnpacker> unpackers) {
        return Flux.defer(() -> Flux.create(sink -> {
            Socket socket;

            try {
                socket = IO.socket("https://streamer.cryptocompare.com");
                logger.info("[EXTERNAL-SERVICE] Connecting to CryptoCompare.com ...");
            } catch (URISyntaxException e) {
                sink.error(e);
                return;
            }

            Runnable closeSocket = () -> {
                socket.close();
                logger.info("[EXTERNAL-SERVICE] Connection to CryptoCompare.com closed");
            };

            socket
                    .on(Socket.EVENT_CONNECT, args -> input.subscribe(
                            v -> {
                                String[] subscription = {v};
                                Map<String, Object> subs = new HashMap<>();
                                subs.put("subs", subscription);
                                socket.emit("SubAdd", subs);
                            },
                            sink::error
                    ))
                    .on("m", args -> {
                        String message = args[0].toString();
                        String messageType = message.substring(0, message.indexOf("~"));
                        for (MessageUnpacker unpacker : unpackers) {
                            if (unpacker.supports(messageType)) {
                                try {
                                    sink.next(unpacker.unpack(message));
                                } catch (Throwable t) {
                                    sink.error(t);
                                    closeSocket.run();
                                }
                                break;
                            }
                        }
                    })
                    .on(Socket.EVENT_ERROR, args -> sink.error((Throwable) args[0]))
                    .on(Socket.EVENT_DISCONNECT, args -> sink.complete());

            sink.onCancel(closeSocket::run);
            socket.connect();
        }, FluxSink.OverflowStrategy.ERROR));
    }

}
