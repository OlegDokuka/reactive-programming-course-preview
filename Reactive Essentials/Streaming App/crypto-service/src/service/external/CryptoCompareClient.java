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

    static Flux<Map<String, Object>> connect(Collection<String> input, Collection<MessageUnpacker> unpackers) {
        /* TODO: convert the following listener API into Flux via Flux.create */
            Socket socket;

            try {
                socket = IO.socket("https://streamer.cryptocompare.com");
                logger.info("[EXTERNAL-SERVICE] Connecting to CryptoCompare.com ...");
            } catch (URISyntaxException e) {
                /* TODO: deliver this error to subscriber using given FluxSink */ 
			 throw new RuntimeException();
            }

            Runnable closeSocket = () -> {
                socket.close();
                logger.info("[EXTERNAL-SERVICE] Connection to CryptoCompare.com closed");
            };

            socket
                    .on(Socket.EVENT_CONNECT, args -> {
                        String[] subscription = input.toArray(new String[0]);
                        Map<String, Object> subs = new HashMap<>();
                        subs.put("subs", subscription);
                        socket.emit("SubAdd", subs);
                    })
                    .on("m", args -> {
                        String message = args[0].toString();
                        String messageType = message.substring(0, message.indexOf("~"));
                        for (MessageUnpacker unpacker : unpackers) {
                            if (unpacker.supports(messageType)) {
                                try {
                                    Map<String, Object> unpackMessage = unpacker.unpack(message);
                                    /* TODO deliver unpacked message to subscriber using given FluxSink */
                                } catch (Throwable t) {
                                    /* TODO: deliver error to subscriber using given FluxSink */
                                    closeSocket.run();
                                }
                                break;
                            }
                        }
                    })
                    .on(Socket.EVENT_ERROR, args -> { /* TODO deliver error to subscriber using given FluxSink */ })
                    .on(Socket.EVENT_DISCONNECT, args -> { /* TODO: deliver complete to subscriber using given FluxSink */ });

            /* TODO: ensure that subscriber cancellation closes socket */
            socket.connect();
        return Flux.error(() -> {
			socket.close();
			return new RuntimeException();
});
    }

}
