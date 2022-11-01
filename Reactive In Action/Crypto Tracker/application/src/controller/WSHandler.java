package controller;

import dto.MessageDTO;
import reactor.core.publisher.Flux;
import service.PriceService;
import service.TradeService;

public class WSHandler {

	private final PriceService priceService;
	private final TradeService tradeService;

	public WSHandler(PriceService priceService, TradeService tradeService) {
		this.priceService = priceService;
		this.tradeService = tradeService;
	}

	public Flux<MessageDTO> handle(Flux<Long> input) {
		return Flux.merge(
				priceService.pricesStream(input),
				tradeService.tradesStream()
		);
	}
}
