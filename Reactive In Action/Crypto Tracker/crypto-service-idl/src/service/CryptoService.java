package service;

import java.util.Map;

import reactor.core.publisher.Flux;

public interface CryptoService {

	Flux<Map<String, Object>> eventsStream();
}
