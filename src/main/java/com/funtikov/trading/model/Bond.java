package com.funtikov.trading.model;

import java.io.Serializable;

public record Bond(String symbol,
                   String name,
                   String country,
                   String currency,
                   String exchange,
                   String mic_code,
                   String type,
                   Access access) implements Serializable {

}
