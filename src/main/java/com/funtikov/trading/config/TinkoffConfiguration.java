package com.funtikov.trading.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Getter;

@ConfigurationProperties("tinkoff")
@Getter
public class TinkoffConfiguration {

    private String apiToken;

}
