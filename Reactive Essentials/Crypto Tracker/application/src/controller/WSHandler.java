package controller;

import dto.MessageDTO;
import reactor.core.publisher.Flux;
import service.PriceService;
import service.TradeService;

public class WSHandler {

	private final PriceService priceService;

	public WSHandler(PriceService priceService) {
		this.priceService = priceService;
	}

	public Flux<MessageDTO<Float>> handle(Flux<Long> input) {
		return priceService.pricesStream(input);
	}
}
