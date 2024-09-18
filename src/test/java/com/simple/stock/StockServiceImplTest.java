package com.simple.stock;

import com.simple.stock.model.Stock;
import com.simple.stock.model.StockType;
import com.simple.stock.model.Trade;
import com.simple.stock.repository.MapBasedRepository;
import com.simple.stock.repository.StockRepository;
import com.simple.stock.service.StockServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class StockServiceImplTest {

    private StockServiceImpl stockServiceImpl;
    private StockRepository stockRepository;

    @BeforeEach
    void setUp() {
        stockRepository = new MapBasedRepository();
        stockServiceImpl = new StockServiceImpl(stockRepository);
    }

    @Test
    void testCalculateDividendYield_CommonStock() {
        Stock stock = Mockito.mock(Stock.class);
        when(stock.getStockType()).thenReturn(StockType.COMMON);
        when(stock.getLastDividend()).thenReturn(10.0);
        when(stock.getSymbol()).thenReturn("POP");
        stockRepository.save(stock);

        double dividendYield = stockServiceImpl.calculateDividendYield("POP", 100.0);

        assertEquals(0.1, dividendYield);
    }

    @Test
    void testCalculateDividendYield_PreferredStock() {
        Stock stock = Mockito.mock(Stock.class);
        when(stock.getStockType()).thenReturn(StockType.PREFERRED);
        when(stock.getFixedDividend()).thenReturn(0.05);
        when(stock.getParValue()).thenReturn(100.0);
        when(stock.getSymbol()).thenReturn("GIN");
        stockRepository.save(stock);

        double dividendYield = stockServiceImpl.calculateDividendYield("GIN", 200.0);

        assertEquals(0.025, dividendYield);
    }

    @Test
    void testCalculatePERatio() {
        Stock stock = Mockito.mock(Stock.class);
        when(stock.getStockType()).thenReturn(StockType.COMMON);
        when(stock.getLastDividend()).thenReturn(10.0);
        when(stock.getSymbol()).thenReturn("POP");
        stockRepository.save(stock);

        double peRatio = stockServiceImpl.calculatePERatio("POP", 100.0);

        assertEquals(10.0, peRatio);
    }

    @Test
    void testRecordTrade() {
        Stock stock = Mockito.mock(Stock.class);
        when(stock.getSymbol()).thenReturn("POP");
        stockRepository.save(stock);

        stockServiceImpl.recordTrade("POP", 100, "buy", 110.0);

        Mockito.verify(stock).recordTrade(any(Trade.class));
    }

    @Test
    void testCalculateVolumeWeightedStockPrice() {
        Stock stock = Mockito.mock(Stock.class);
        when(stock.calculateVolumeWeightedStockPrice()).thenReturn(105.0);
        when(stock.getSymbol()).thenReturn("POP");
        stockRepository.save(stock);

        double vwsp = stockServiceImpl.calculateVolumeWeightedStockPrice("POP");

        assertEquals(105.0, vwsp);
    }

    @Test
    void testCalculateAllShareIndex() {
        stockRepository.getAll().clear();
        Stock stock1 = Mockito.mock(Stock.class);
        Stock stock2 = Mockito.mock(Stock.class);
        when(stock1.calculateVolumeWeightedStockPrice()).thenReturn(100.0);
        when(stock2.calculateVolumeWeightedStockPrice()).thenReturn(150.0);
        when(stock1.getSymbol()).thenReturn("POP");
        stockRepository.save(stock1);
        when(stock2.getSymbol()).thenReturn("GIN");
        stockRepository.save(stock2);

        double allShareIndex = stockServiceImpl.calculateAllShareIndex();

        assertEquals(Math.sqrt(100.0 * 150.0), allShareIndex);
    }
}
