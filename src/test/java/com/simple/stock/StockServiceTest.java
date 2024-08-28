package com.simple.stock;

import com.simple.stock.model.Stock;
import com.simple.stock.model.StockType;
import com.simple.stock.model.Trade;
import com.simple.stock.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class StockServiceTest {

    private StockService stockService;
    private Map<String, Stock> stockRepository;

    @BeforeEach
    void setUp() {
        stockRepository = new HashMap<>();
        stockService = new StockService(stockRepository);
    }

    @Test
    void testCalculateDividendYield_CommonStock() {
        Stock stock = Mockito.mock(Stock.class);
        when(stock.getStockType()).thenReturn(StockType.COMMON);
        when(stock.getLastDividend()).thenReturn(10.0);
        stockRepository.put("POP", stock);

        double dividendYield = stockService.calculateDividendYield("POP", 100.0);

        assertEquals(0.1, dividendYield);
    }

    @Test
    void testCalculateDividendYield_PreferredStock() {
        Stock stock = Mockito.mock(Stock.class);
        when(stock.getStockType()).thenReturn(StockType.PREFERRED);
        when(stock.getFixedDividend()).thenReturn(0.05);
        when(stock.getParValue()).thenReturn(100.0);
        stockRepository.put("GIN", stock);

        double dividendYield = stockService.calculateDividendYield("GIN", 200.0);

        assertEquals(0.025, dividendYield);
    }

    @Test
    void testCalculatePERatio() {
        Stock stock = Mockito.mock(Stock.class);
        when(stock.getStockType()).thenReturn(StockType.COMMON);
        when(stock.getLastDividend()).thenReturn(10.0);
        stockRepository.put("POP", stock);

        double peRatio = stockService.calculatePERatio("POP", 100.0);

        assertEquals(10.0, peRatio);
    }

    @Test
    void testRecordTrade() {
        Stock stock = Mockito.mock(Stock.class);
        stockRepository.put("POP", stock);

        stockService.recordTrade("POP", 100, "buy", 110.0);

        Mockito.verify(stock).recordTrade(any(Trade.class));
    }

    @Test
    void testCalculateVolumeWeightedStockPrice() {
        Stock stock = Mockito.mock(Stock.class);
        when(stock.calculateVolumeWeightedStockPrice()).thenReturn(105.0);
        stockRepository.put("POP", stock);

        double vwsp = stockService.calculateVolumeWeightedStockPrice("POP");

        assertEquals(105.0, vwsp);
    }

    @Test
    void testCalculateAllShareIndex() {
        Stock stock1 = Mockito.mock(Stock.class);
        Stock stock2 = Mockito.mock(Stock.class);
        when(stock1.calculateVolumeWeightedStockPrice()).thenReturn(100.0);
        when(stock2.calculateVolumeWeightedStockPrice()).thenReturn(150.0);
        stockRepository.put("POP", stock1);
        stockRepository.put("GIN", stock2);

        double allShareIndex = stockService.calculateAllShareIndex();

        assertEquals(Math.sqrt(100.0 * 150.0), allShareIndex);
    }
}
