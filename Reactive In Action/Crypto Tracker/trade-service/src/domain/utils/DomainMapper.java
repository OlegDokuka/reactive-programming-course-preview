package domain.utils;

import java.util.HashMap;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Trade;
import dto.MessageDTO;
import org.bson.Document;

public class DomainMapper {


	public static Trade mapToDomain(MessageDTO<MessageDTO.Trade> tradeMessageDTO) {
		Trade trade = new Trade();

		trade.setId(UUID.randomUUID().toString());
		trade.setPrice(tradeMessageDTO.getData().getPrice());
		trade.setAmount(tradeMessageDTO.getData().getAmount());
		trade.setCurrency(tradeMessageDTO.getCurrency());
		trade.setMarket(tradeMessageDTO.getMarket());
		trade.setTimestamp(tradeMessageDTO.getTimestamp());

		return trade;
	}

	public static Document mapToMongoDocument(Trade trade) {
		return new Document(
				new ObjectMapper()
						.convertValue(trade, new TypeReference<HashMap<String, Object>>() {})
		);
	}
}
