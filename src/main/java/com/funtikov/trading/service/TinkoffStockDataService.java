package com.funtikov.trading.service;

import com.funtikov.trading.util.StatisticsCalculator;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Singleton
@RequiredArgsConstructor
public class TinkoffStockDataService {

    private final TinkoffApiService tinkoffApiService;

    public CompletableFuture<List<HistoricCandle>> getStockCandles(String figi) {
        return tinkoffApiService.getInvestApi()
                .getMarketDataService()
                .getCandles(figi,
                        Instant.now().minusSeconds(3600),
                        Instant.now(),
                        CandleInterval.CANDLE_INTERVAL_1_MIN);
    }

    public CompletableFuture<BigDecimal> getAveragePrice(String figi) {
        return getStockCandles(figi).thenApply(StatisticsCalculator::calculateAveragePrice);
    }

}
