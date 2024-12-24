package com.funtikov.trading.controller;

import com.funtikov.trading.service.TinkoffStockDataService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import lombok.RequiredArgsConstructor;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller("/api/v1/stock")
@RequiredArgsConstructor
public class TinkoffStockController {

    private final TinkoffStockDataService stockDataService;

    @Get("/candies")
    public CompletableFuture<List<HistoricCandle>> getCandles(@QueryValue String figi) {
        return stockDataService.getStockCandles(figi);
    }

    @Get("/average")
    public CompletableFuture<BigDecimal> getAveragePrice(@QueryValue String figi) {
        return stockDataService.getAveragePrice(figi);
    }

}
