package service;

import dto.MessageDTO;
import reactor.core.publisher.Flux;

public interface TradeService {

	Flux<MessageDTO<MessageDTO.Trade>> tradesStream();

}
