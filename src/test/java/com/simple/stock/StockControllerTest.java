package com.simple.stock;
import com.simple.stock.controller.StockController;
import com.simple.stock.model.TradeRequest;
import com.simple.stock.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StockControllerTest {

    @Mock
    private StockService stockService;

    @InjectMocks
    private StockController stockController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(stockController).build();
    }

    @Test
    void testGetDividendYield() throws Exception {
        when(stockService.calculateDividendYield("POP", 120.0)).thenReturn(0.1);

        mockMvc.perform(get("/stocks/POP/dividendYield")
                        .param("price", "120.0"))
                .andExpect(status().isOk())
                .andExpect(content().string("0.1"));


        verify(stockService).calculateDividendYield("POP", 120.0);
    }

    @Test
    void testGetPERatio() throws Exception {
        when(stockService.calculatePERatio("POP", 120.0)).thenReturn(15.0);

        mockMvc.perform(get("/stocks/POP/peRatio")
                        .param("price", "120.0"))
                .andExpect(status().isOk())
                .andExpect(content().string("15.0"));;

        verify(stockService).calculatePERatio("POP", 120.0);
    }

    @Test
    void testRecordTrade() throws Exception {
        TradeRequest tradeRequest = new TradeRequest(100, "buy", 110.0);

        mockMvc.perform(post("/stocks/POP/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\":100,\"indicator\":\"buy\",\"price\":110.0}"))
                .andExpect(status().isOk());

        verify(stockService).recordTrade("POP", tradeRequest.quantity(), tradeRequest.indicator(), tradeRequest.price());
    }

    @Test
    void testGetVolumeWeightedStockPrice() throws Exception {
        when(stockService.calculateVolumeWeightedStockPrice("POP")).thenReturn(115.0);

        mockMvc.perform(get("/stocks/POP/vwsp"))
                .andExpect(status().isOk())
                .andExpect(content().string("115.0"));;

        verify(stockService).calculateVolumeWeightedStockPrice("POP");
    }

    @Test
    void testGetAllShareIndex() throws Exception {
        when(stockService.calculateAllShareIndex()).thenReturn(150.0);

        mockMvc.perform(get("/stocks/allShareIndex"))
                .andExpect(status().isOk())
                .andExpect(content().string("150.0"));;

        verify(stockService).calculateAllShareIndex();
    }
}
