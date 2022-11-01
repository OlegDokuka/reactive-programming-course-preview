
import java.io.IOException;
import java.util.function.BiFunction;

import com.example.part_10.utils.NettyUtils;
import com.mongodb.reactivestreams.client.MongoClients;
import controller.WSHandler;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.websocket.WebsocketInbound;
import reactor.netty.http.websocket.WebsocketOutbound;
import reactor.util.Loggers;
import repository.TradeRepository;
import repository.impl.H2TradeRepository;
import repository.impl.MongoTradeRepository;
import service.CryptoService;
import service.PriceService;
import service.TradeService;
import service.external.CryptoCompareService;
import service.impl.DefaultPriceService;
import service.impl.DefaultTradeService;
import utils.EmbeddedMongo;
import utils.JsonUtils;
import utils.MetricsConfig;

import static utils.H2Helper.createInMemH2;
import static utils.HttpResourceResolver.resourcePath;

public class ApplicationRunner {

	private static final Logger logger = LoggerFactory.getLogger("http-server");

	public static void main(String[] args) throws IOException {

		EmbeddedMongo.run();
		Loggers.useSl4jLoggers();
		MetricsConfig.init();
		// TODO: Integrate Metrics reporting for all the services using Flux#metrics

		CryptoService cryptoCompareService = new CryptoCompareService();
		TradeRepository h2Repository = new H2TradeRepository(createInMemH2());
		TradeRepository mongoRepository = new MongoTradeRepository(MongoClients.create());
		PriceService defaultPriceService = new DefaultPriceService(cryptoCompareService);
		TradeService defaultTradeService = new DefaultTradeService(cryptoCompareService, h2Repository, mongoRepository);
		WSHandler handler = new WSHandler(defaultPriceService, defaultTradeService);

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
			          // TODO Enable stream metrics
			          .doOnNext(inMessage -> logger.info("[WS] >> " + inMessage))
			          .transform(ApplicationRunner::handleRequestedAveragePriceIntervalValue)
			          .transform(handler::handle)
			          .map(JsonUtils::writeAsString)
			          .doOnNext(outMessage -> logger.info("[WS] << " + outMessage))
			          .transform(ApplicationRunner::handleOutgoingStreamBackpressure)
			          .transform(NettyUtils.prepareOutbound(res));
	}

	// Visible for testing
	public static Flux<Long> handleRequestedAveragePriceIntervalValue(Flux<String> requestedInterval) {
		// TODO: input may be incorrect, pass only correct interval
		// TODO: ignore invalid values (empty, non number, <= 0, > 60)
		return Flux.never();
	}

	// Visible for testing
	public static Flux<String> handleOutgoingStreamBackpressure(Flux<String> outgoingStream) {
		// TODO: Add backpressure handling
		// It is possible that writing data to output may be slower than rate of
		// incoming output data

		return outgoingStream; // TODO Enable Backpressure
	}


}
