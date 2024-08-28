package com.simple.stock.model;

import java.util.Date;

public record Trade(Date timestamp, int quantity, String indicator, double price) {
}


