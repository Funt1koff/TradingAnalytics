package com.funtikov.trading.client;

import com.funtikov.trading.model.BatchResponse;
import com.funtikov.trading.model.BondsResponse;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Client("${integration.twelvedata.rest-url}")
public interface TwelveDataClient {

    @Get("/time_series{?symbol,interval,apikey,start_date,end_date}")
    Mono<BatchResponse> getBatchData(
            String symbol,
            String interval,
            String apikey,
            String start_date,
            String end_date
    );

    @Get("/bonds{?symbol,exchange,country,format,delimiter,show_plan,page,outputsize,apikey}")
    Flux<String> getBondsData(
            @Nullable String symbol,
            @Nullable String exchange,
            @Nullable String country,
            @Nullable String format,
            @Nullable String delimiter,
            @Nullable String show_plan,
            @Nullable String page,
            @Nullable String outputsize,
            String apikey
    );

}
