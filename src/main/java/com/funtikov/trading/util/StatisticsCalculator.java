package com.funtikov.trading.util;

import ru.tinkoff.piapi.contract.v1.HistoricCandle;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class StatisticsCalculator {

    public static BigDecimal calculateAveragePrice(List<HistoricCandle> candles) {
        BigDecimal total = BigDecimal.ZERO;
        int count = 0;

        for (HistoricCandle candle : candles) {
            BigDecimal open = new BigDecimal(candle.getOpen().getUnits());
            BigDecimal close = new BigDecimal(candle.getClose().getUnits());
            total = total.add(open).add(close);
            count += 2;
        }

        return total.divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP);
    }
}
