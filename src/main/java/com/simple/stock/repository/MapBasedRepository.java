package com.simple.stock.repository;

import com.simple.stock.model.Stock;
import com.simple.stock.model.StockType;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository("mapBasedRepository")
public class MapBasedRepository implements StockRepository{
    private Map<String, Stock> container;

    public MapBasedRepository() {
        // Initialize with some stocks
        container = new HashMap<>();
        container.put("TEA",new Stock("TEA", StockType.COMMON, 0, null, 100));
        container.put("POP",new Stock("POP", StockType.COMMON, 8, null, 100));
        container.put("ALE",new Stock("ALE", StockType.COMMON, 23, null, 60));
        container.put("GIN",new Stock("GIN", StockType.PREFERRED, 8, 0.02, 100));
        container.put("JOE",new Stock("JOE", StockType.COMMON, 13, null, 250));
    }

    @Override
    public Optional<Stock> findBySymbol(String symbol) {
        return Optional.ofNullable(container.get(symbol));
    }

    @Override
    public Collection<Stock> getAll() {
        return container.values();
    }

    @Override
    public void save(Stock stock) {
        container.put(stock.getSymbol(), stock);
    }
}
