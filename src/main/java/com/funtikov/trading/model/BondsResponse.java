package com.funtikov.trading.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BondsResponse(Result result,
                            String status) implements Serializable {

}
