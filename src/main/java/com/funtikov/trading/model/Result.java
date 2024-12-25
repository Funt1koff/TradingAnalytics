package com.funtikov.trading.model;

import java.io.Serializable;
import java.util.List;

public record Result(long count,
                     List<Bond> list) implements Serializable {

}
