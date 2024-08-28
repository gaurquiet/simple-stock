package com.simple.stock.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
public class Stock {
    private String symbol;
    private StockType stockType = StockType.COMMON;
    private double lastDividend;
    private Double fixedDividend;  // Can be null for common stocks
    private double parValue;
    private List<Trade> trades = new ArrayList<>();

    public Stock(String symbol, StockType type, double lastDividend, Double fixedDividend, double parValue) {
        this.symbol = symbol;
        this.stockType = type;
        this.lastDividend = lastDividend;
        this.fixedDividend = fixedDividend;
        this.parValue = parValue;
    }

    public void recordTrade(Trade trade) {
        trades.add(trade);
    }

    public double calculateVolumeWeightedStockPrice() {
        Date now = new Date();
        Date fiveMinutesAgo = new Date(now.getTime() - TimeUnit.MINUTES.toMillis(5));

        double totalTradedPriceQuantity = 0.0;
        double totalQuantity = 0.0;

        for (Trade trade : trades) {
            if (trade.timestamp().after(fiveMinutesAgo)) {
                totalTradedPriceQuantity += trade.price() * trade.quantity();
                totalQuantity += trade.quantity();
            }
        }

        if (totalQuantity == 0) {
            return 0.0;
        }
        return totalTradedPriceQuantity / totalQuantity;
    }

}
