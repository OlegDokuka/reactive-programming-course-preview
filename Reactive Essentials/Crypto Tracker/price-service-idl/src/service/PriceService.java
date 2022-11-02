package service;

import dto.MessageDTO;
import reactor.core.publisher.Flux;

public interface PriceService {

	Flux<MessageDTO<Float>> pricesStream(Flux<Long> intervalPreferencesStream);
}
