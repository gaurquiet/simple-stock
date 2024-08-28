package com.simple.stock.repository;

import com.simple.stock.model.Stock;

import java.util.Collection;
import java.util.Optional;

public interface StockRepository {
    Optional<Stock> findBySymbol(String symbol);
    Collection<Stock> getAll();
    void save(Stock stock);
}
