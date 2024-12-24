package com.funtikov.trading.service;

import com.funtikov.trading.config.TinkoffConfiguration;
import jakarta.inject.Singleton;
import lombok.Getter;
import ru.tinkoff.piapi.core.InvestApi;

@Getter
@Singleton
public class TinkoffApiService {

    private final InvestApi investApi;

    public TinkoffApiService(TinkoffConfiguration config) {
        this.investApi = InvestApi.create(config.getApiToken());
    }
}
