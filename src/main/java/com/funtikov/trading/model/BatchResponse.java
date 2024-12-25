package com.funtikov.trading.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BatchResponse {

    private Map<String, SymbolResponse> data;

    @JsonProperty("is_batch")
    private boolean isBatch;

    public boolean isBatch() {
        return isBatch;
    }

    public void setBatch(boolean batch) {
        isBatch = batch;
    }

    @Data
    public static class SymbolResponse {
        private MetaData meta;
        private List<Values> values;
        private String status;

    }

    @Data
    public static class MetaData {
        @JsonProperty("symbol")
        private String symbol;
        @JsonProperty("interval")
        private String interval;
        @JsonProperty("currency")
        private String currency;

    }

    @Data
    public static class Values {
        private String datetime;
        private String open;
        private String high;
        private String low;
        private String close;
        private String volume;

    }
}
