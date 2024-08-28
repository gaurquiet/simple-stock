package com.simple.stock.utils;

public class StockNotFoundException extends RuntimeException {

    public StockNotFoundException(String symbol) {
        super("Stock with symbol " + symbol + " not found.");
    }
}
