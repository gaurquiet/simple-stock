package com.simple.stock.service;

import com.simple.stock.model.Stock;
import com.simple.stock.model.StockType;
import com.simple.stock.model.Trade;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class StockService {

    private Map<String, Stock> stockRepository;

    public StockService() {
        // Initialize with some stocks
        stockRepository = new HashMap<>();
        stockRepository.put("TEA",new Stock("TEA", StockType.COMMON, 0, null, 100));
        stockRepository.put("POP",new Stock("POP", StockType.COMMON, 8, null, 100));
        stockRepository.put("ALE",new Stock("ALE", StockType.COMMON, 23, null, 60));
        stockRepository.put("GIN",new Stock("GIN", StockType.PREFERRED, 8, 0.02, 100));
        stockRepository.put("JOE",new Stock("JOE", StockType.COMMON, 13, null, 250));
    }

    public StockService(Map<String, Stock> stockRepository) {
        this.stockRepository = stockRepository;
    }

    public double calculateDividendYield(String symbol, double price) {
        Stock stock = stockRepository.get(symbol);
        if (stock.getStockType() == StockType.COMMON) {
            return stock.getLastDividend() / price;
        } else {
            return (stock.getFixedDividend() * stock.getParValue()) / price;
        }
    }

    public double calculatePERatio(String symbol, double price) {
        double dividend = calculateDividendYield(symbol, price) * price;
        return dividend == 0 ? 0 : price / dividend;
    }

    public void recordTrade(String symbol, int quantity, String indicator, double price) {
        Stock stock = stockRepository.get(symbol);
        if (stock != null) {
            stock.recordTrade(new Trade(new Date(), quantity, indicator, price));
        } else {
            throw new IllegalArgumentException("Stock symbol not found");
        }
    }

    public double calculateVolumeWeightedStockPrice(String symbol) {
        Stock stock = stockRepository.get(symbol);
        if (stock != null) {
            return stock.calculateVolumeWeightedStockPrice();
        }
        throw new IllegalArgumentException("Stock symbol not found");
    }

    public double calculateAllShareIndex() {
        Collection<Stock> stocks = stockRepository.values();

        double productOfVWSP = 1.0;
        int numberOfStocks = stocks.size();

        for (Stock stock : stocks) {
            double vwsp = stock.calculateVolumeWeightedStockPrice();
            if (vwsp > 0) {
                productOfVWSP *= vwsp;
            }
        }

        return Math.pow(productOfVWSP, 1.0 / numberOfStocks);
    }

}
