package com.simple.stock.controller;

import com.simple.stock.model.TradeRequest;
import com.simple.stock.service.StockService;
import com.simple.stock.service.StockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/{symbol}/dividendYield")
    public double getDividendYield(@PathVariable String symbol, @RequestParam double price) {
        return stockService.calculateDividendYield(symbol, price);
    }

    @GetMapping("/{symbol}/peRatio")
    public double getPERatio(@PathVariable String symbol, @RequestParam double price) {
        return stockService.calculatePERatio(symbol, price);
    }

    @PostMapping("/{symbol}/trade")
    public void recordTrade(@PathVariable String symbol, @RequestBody TradeRequest tradeRequest) {
        stockService.recordTrade(symbol, tradeRequest.quantity(), tradeRequest.indicator(),
                tradeRequest.price()
        );
    }

    @GetMapping("/{symbol}/vwsp")
    public double getVolumeWeightedStockPrice(@PathVariable String symbol) {
        return stockService.calculateVolumeWeightedStockPrice(symbol);
    }

    @GetMapping("/allShareIndex")
    public double getAllShareIndex() {
        return stockService.calculateAllShareIndex();
    }
}
