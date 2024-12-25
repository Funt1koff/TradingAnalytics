package com.funtikov.trading.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.funtikov.trading.client.TwelveDataClient;
import com.funtikov.trading.model.BatchResponse;
import com.funtikov.trading.model.Bond;
import com.funtikov.trading.model.BondsResponse;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Nullable;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class TwelveDataService {

    private final TwelveDataClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${integration.twelvedata.apikey}")
    private String apiKey;

    public Mono<BatchResponse> getBatchData(String symbol, String interval, String start_date, String end_date) {
        return client.getBatchData(symbol, interval, apiKey, start_date, end_date);
    }


    public Flux<Bond> getBondsData(@Nullable String symbol,
                                   @Nullable String exchange,
                                   @Nullable String country,
                                   @Nullable String format,
                                   @Nullable String delimiter,
                                   @Nullable String show_plan,
                                   @Nullable String page,
                                   @Nullable String outputsize) {
        return client.getBondsData(symbol, exchange, country, format, delimiter, show_plan, page, outputsize, apiKey)
                .flatMap(response -> {
                    log.info("Response from bonds: {}", response);
                    try {
                        return Flux.fromIterable(mapper.readValue(response, BondsResponse.class).result().list());
                    } catch (Exception e) {
                        return Flux.error(new IllegalArgumentException("Failed to parse response", e));
                    }
                });
    }
}
