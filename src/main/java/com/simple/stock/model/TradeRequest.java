package com.simple.stock.model;

public record TradeRequest(int quantity, String indicator, double price) {
}
