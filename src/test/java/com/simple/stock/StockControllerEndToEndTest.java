package com.simple.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;

@SpringBootTest(classes = SuperSimpleStockApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test") // Use a separate profile for testing
class StockControllerEndToEndTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void testGetDividendYield() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/stocks/POP/dividendYield")
                        .param("price", "150.00")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    BigDecimal dividendYield = new BigDecimal(responseContent);
                    assertThat(dividendYield.doubleValue()).isCloseTo(0.0533, within(0.0001)); // Allow for minor floating-point differences
                });
    }

    @Test
    void testGetDividendYieldNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/stocks/INVALID_SYMBOL/dividendYield")
                        .param("price", "150.00")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Stock with symbol INVALID_SYMBOL not found."));
    }

    @Test
    void testRecordTrade() throws Exception {
        String tradeRequestJson = """
                {
                  "quantity": 100,
                  "indicator": "buy",
                  "price": 150.0
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/stocks/POP/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tradeRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

}
