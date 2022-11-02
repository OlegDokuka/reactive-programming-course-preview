import com.example.part_10.utils.NettyUtils;
import controller.WSHandler;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.websocket.WebsocketInbound;
import reactor.netty.http.websocket.WebsocketOutbound;
import service.CryptoService;
import service.PriceService;
import service.external.CryptoCompareService;
import service.impl.DefaultPriceService;
import utils.JsonUtils;

import java.io.IOException;
import java.util.function.BiFunction;

import static utils.HttpResourceResolver.resourcePath;

public class ApplicationRunner {

	private static final Logger logger = LoggerFactory.getLogger("http-server");

	public static void main(String[] args) throws IOException {

		CryptoService cryptoCompareService = new CryptoCompareService();
		PriceService defaultPriceService = new DefaultPriceService(cryptoCompareService);
		WSHandler handler = new WSHandler(defaultPriceService);

		HttpServer.create()
		          .host("localhost")
		          .port(8080)
		          .route(hsr ->
			          hsr.ws("/stream", handleWebsocket(handler))
                         .file("/favicon.ico", resourcePath("ui/favicon.ico"))
                         .file("/main.js", resourcePath("ui/main.js"))
                         .file("/", resourcePath("ui/index.html"))
                         .file("/**", resourcePath("ui/index.html"))
		          )
		          .bindNow()
		          .onDispose()
		          .block();
	}

	private static BiFunction<WebsocketInbound, WebsocketOutbound, Publisher<Void>> handleWebsocket(WSHandler handler) {
		return (req, res) ->
			NettyUtils.prepareInput(req)
			          .name("My Lovely Flux 1")
			          .tag("My Super Important Key", "My Super Important Value")
			          .metrics()
			          .doOnNext(inMessage -> logger.info("[WS] >> " + inMessage))
			          .transform(ApplicationRunner::handleRequestedAveragePriceIntervalValue)
			          .transform(handler::handle)
			          .map(JsonUtils::writeAsString)
			          .doOnNext(outMessage -> logger.info("[WS] << " + outMessage))
			          .transform(NettyUtils.prepareOutbound(res));
	}

	// Visible for testing
	public static Flux<Long> handleRequestedAveragePriceIntervalValue(Flux<String> requestedInterval) {
		return Flux.never();
	}
}
