package com.funtikov.trading.controller;

import com.funtikov.trading.model.BatchResponse;
import com.funtikov.trading.model.Bond;
import com.funtikov.trading.service.TwelveDataService;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller("/api/v1/twelvedata")
@RequiredArgsConstructor
@Slf4j
public class TwelveDataController {

    private final TwelveDataService twelveDataService;


    @Get(value = "/batch{?symbols,interval,format,filename}", produces = MediaType.APPLICATION_JSON_STREAM)
    public Mono<BatchResponse> getBatch(String symbols, String interval, String start_date, String end_date) {
        log.info("Getting batch data for symbols: {}, interval: {}, start_date: {}, end_date: {}", symbols, interval, start_date, end_date);
        return twelveDataService.getBatchData(symbols, interval, start_date, end_date);
    }

    @Get(value = "/bonds{?symbol,exchange,country,format,delimiter,show_plan,page,outputsize}", produces = MediaType.APPLICATION_JSON_STREAM)
    public Flux<Bond> getBonds(@Nullable String symbol,
                               @Nullable String exchange,
                               @Nullable String country,
                               @Nullable String format,
                               @Nullable String delimiter,
                               @Nullable String show_plan,
                               @Nullable String page,
                               @Nullable String outputsize) {
        log.info("Getting bonds data for symbol: {}, exchange: {}, country: {}, format: {}, delimiter: {}, show_plan: {}, page: {}, outputsize: {}", symbol, exchange, country, format, delimiter, show_plan, page, outputsize);
        return twelveDataService.getBondsData(symbol, exchange, country, format, delimiter, show_plan, page, outputsize);
    }
}
