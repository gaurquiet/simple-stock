package com.simple.stock;

import com.simple.stock.controller.StockController;
import com.simple.stock.service.StockService;
import com.simple.stock.utils.StockNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockService stockService;

    @Test
    void handleStockNotFoundException() throws Exception {

        when(stockService.calculateDividendYield("invalidSymbol", 100.0))
                .thenThrow(new StockNotFoundException("invalidSymbol"));
        mockMvc.perform(get("/stocks/invalidSymbol/dividendYield")
                        .param("price", "100"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Stock with symbol invalidSymbol not found."));
    }

    @Test
    void handleGlobalException() throws Exception {
        mockMvc.perform(get("/stocks/triggerException"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred."));
    }
}
