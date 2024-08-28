package com.simple.stock.service;

public interface StockService {

    double calculateDividendYield(String symbol, double price);

    double calculatePERatio(String symbol, double price);

    void recordTrade(String symbol, int quantity, String indicator, double price);

    double calculateVolumeWeightedStockPrice(String symbol);

    double calculateAllShareIndex();
}
