package com.funtikov.trading.model;

import java.io.Serializable;

public record Access(String global,
                     String plan) implements Serializable {

}
