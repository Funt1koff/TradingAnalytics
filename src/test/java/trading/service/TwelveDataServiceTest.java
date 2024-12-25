package trading.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.funtikov.trading.client.TwelveDataClient;
import com.funtikov.trading.model.Bond;
import com.funtikov.trading.service.TwelveDataService;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static trading.service.TwelveDataServiceTest.MockJsons.mockNullQueryJsonResponse;

@MicronautTest
public class TwelveDataServiceTest {

    @Inject
    private TwelveDataService twelveDataService;

    @Inject
    private TwelveDataClient twelveDataClient;

    @Inject
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test without parameters")
    void testGetBondsData() throws Exception {

        Mockito.when(twelveDataClient.getBondsData(
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()
        )).thenReturn(Flux.just(mockNullQueryJsonResponse));

        Flux<Bond> bondsFlux = twelveDataService.getBondsData(null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        bondsFlux.collectList().subscribe(bonds -> {
            assertEquals(3, bonds.size());
            assertEquals("Quayside Holdings Ltd", bonds.get(0).name());
            assertEquals("US Treasury Yield 2 Years", bonds.get(1).name());
            assertEquals("South Africa 10 Year Government Bond", bonds.get(2).name());
        });

        Mockito.verify(twelveDataClient, Mockito.times(1)).getBondsData(
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()
        );
    }

    @Test
    @DisplayName("Test with correct parameter")
    public void testGetBondsDataWithSymbol() {

        Mockito.when(twelveDataClient.getBondsData(
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()
        )).thenReturn(Flux.just(MockJsons.mockSymbolUS2YQueryJsonResponse));

        Flux<Bond> bondsFlux = twelveDataService.getBondsData("US2Y",
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        bondsFlux.collectList().subscribe(bonds -> {
            assertEquals(1, bonds.size());
            assertEquals("US Treasury Yield 2 Years", bonds.get(0).name());
            assertEquals("US2Y", bonds.get(0).symbol());
        });
    }

    @Test
    @DisplayName("Test with incorrect parameter")
    public void testGetBondsDataWithIncorrectParameter() {

        Mockito.when(twelveDataClient.getBondsData(
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()
        )).thenReturn(Flux.just(""));

        Flux<Bond> bondsFlux = twelveDataService.getBondsData("1337",
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        bondsFlux.collectList().subscribe(bonds -> {
            assertEquals(1, bonds.size());
            assertEquals("US Treasury Yield 2 Years", bonds.get(0).name());
            assertEquals("US2Y", bonds.get(0).symbol());
        });
    }

    @MockBean(TwelveDataClient.class)
    TwelveDataClient mockTwelveDataClient() {
        return Mockito.mock(TwelveDataClient.class);
    }


    static class MockJsons {
        public static final String mockNullQueryJsonResponse = """
                {
                  "status": "ok",
                  "meta": {
                    "symbol": "AAPL",
                    "interval": "1day",
                    "currency": "USD",
                    "exchange_timezone": "America/New_York",
                    "exchange": "NASDAQ",
                    "type": "Common Stock"
                  },
                  "values": [
                    {
                      "datetime": "2021-09-01",
                      "open": "153.76",
                      "high": "154.98",
                      "low": "152.34",
                      "close": "152.51",
                      "volume": "80313700"
                    },
                    {
                      "datetime": "2021-09-02",
                      "open": "153.87",
                      "high": "154.72",
                      "low": "153.14",
                      "close": "153.65",
                      "volume": "71115500"
                    },
                    {
                      "datetime": "2021-09-03",
                      "open": "153.76",
                      "high": "154.63",
                      "low": "153.09",
                      "close": "154.30",
                      "volume": "57808700"
                    }
                  ]
                }
                """;

        public static String mockSymbolUS2YQueryJsonResponse = """
                {
                	"result": {
                		"count": 1,
                		"list": [
                			{
                				"symbol": "US2Y",
                				"name": "US Treasury Yield 2 Years",
                				"country": "United States",
                				"currency": "USD",
                				"exchange": "NYSE",
                				"mic_code": "XNYS",
                				"type": "Bond"
                			}
                		]
                	},
                	"status": "ok"
                }
                """;
    }
}
