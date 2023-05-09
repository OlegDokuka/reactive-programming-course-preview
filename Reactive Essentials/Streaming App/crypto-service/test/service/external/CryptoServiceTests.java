package service.external;

import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import reactor.test.StepVerifier;
import service.external.utils.PriceMessageUnpacker;

public class CryptoServiceTests {


    StepVerifier.Step<Map<String, Object>> priceValidationSetup(StepVerifier.FirstStep<Map<String, Object>> stepVerifier) {
        return /* TODO: write set of expectations using stepVerifier */ stepVerifier;
    }

    @Test
    public void testPriceEventsConsumption() throws URISyntaxException {
        Socket socket = Mockito.spy(IO.socket("https://streamer.cryptocompare.com"));
        try (MockedStatic<IO> utilities = Mockito.mockStatic(IO.class)) {
            utilities.when(() -> IO.socket(Mockito.anyString())).thenReturn(socket);
            priceValidationSetup(StepVerifier.create(CryptoCompareClient.connect(Arrays.asList("5~CCCAGG~BTC~USD"), Arrays.asList(new PriceMessageUnpacker())).take(10).log()))
                    .expectComplete()
                    .verify(Duration.ofSeconds(30));
        }

        Mockito.verify(socket).close();
    }
}