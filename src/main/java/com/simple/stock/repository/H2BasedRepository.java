package com.simple.stock.repository;

import com.simple.stock.model.Stock;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository("h2BasedRepository")
public class H2BasedRepository implements StockRepository{

    @Override
    public Optional<Stock> findBySymbol(String symbol) {
        return null;
    }

    @Override
    public Collection<Stock> getAll() {
        return null;
    }

    @Override
    public void save(Stock stock) {

    }
}
