package service.utils;

import java.util.Map;

import dto.MessageDTO;

public class MessageMapper {
    public static final String TYPE_KEY      = "TYPE";
    public static final String PRICE_KEY     = "PRICE";
    public static final String CURRENCY_KEY  = "FROMSYMBOL";
    public static final String MARKET_KEY    = "MARKET";

    public static MessageDTO<Float> mapToPriceMessage(Map<String, Object> event) {
        return MessageDTO.price(
                (float) event.get(PRICE_KEY),
                (String) event.get(CURRENCY_KEY),
                (String) event.get(MARKET_KEY)
        );
    }

    public static boolean isPriceMessageType(Map<String, Object> event) {
        return event.containsKey(TYPE_KEY) &&
                event.get(TYPE_KEY).equals("5");
    }

    public static boolean isValidPriceMessage(Map<String, Object> event) {
        return event.containsKey(PRICE_KEY) &&
                event.containsKey(CURRENCY_KEY) &&
                event.containsKey(MARKET_KEY);
    }
}
