package com.simple.stock.service;

import com.simple.stock.model.Stock;
import com.simple.stock.model.StockType;
import com.simple.stock.model.Trade;
import com.simple.stock.repository.StockRepository;
import com.simple.stock.utils.StockNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

@Service
public class StockServiceImpl implements StockService{

    @Autowired
    @Qualifier("mapBasedRepository")
    private StockRepository stockRepository;

    public StockServiceImpl( @Qualifier("mapBasedRepository")StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public double calculateDividendYield(String symbol, double price) {
        Stock stock = stockRepository.findBySymbol(symbol).orElseThrow(
                () -> new StockNotFoundException(symbol));
        if (stock.getStockType() == StockType.COMMON) {
            return stock.getLastDividend() / price;
        } else {
            return (stock.getFixedDividend() * stock.getParValue()) / price;
        }
    }

    @Override
    public double calculatePERatio(String symbol, double price) {
        double dividend = calculateDividendYield(symbol, price) * price;
        return dividend == 0 ? 0 : price / dividend;
    }

    @Override
    public void recordTrade(String symbol, int quantity, String indicator, double price) {
        Stock stock = stockRepository.findBySymbol(symbol).orElseThrow(
                () -> new StockNotFoundException(symbol));
        if (stock != null) {
            stock.recordTrade(new Trade(new Date(), quantity, indicator, price));
        } else {
            throw new IllegalArgumentException("Stock symbol not found");
        }
    }

    @Override
    public double calculateVolumeWeightedStockPrice(String symbol) {
        Stock stock = stockRepository.findBySymbol(symbol).orElseThrow(
                () -> new StockNotFoundException(symbol));
        if (stock != null) {
            return stock.calculateVolumeWeightedStockPrice();
        }
        throw new IllegalArgumentException("Stock symbol not found");
    }

    @Override
    public double calculateAllShareIndex() {
        Collection<Stock> stocks = stockRepository.getAll();

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
