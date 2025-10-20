package ru.netology.springBootDemo.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.netology.springBootDemo.service.TransferService;
import ru.netology.springBootDemo.model.TransferResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class TransferControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransferService transferService;

    @InjectMocks
    private TransferController transferController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transferController).build();
    }


    @Test
    void transfer_ShouldReturnOk_WhenValidRequest() throws Exception {
        // Given
        when(transferService.transfer(any())).thenReturn(new TransferResponse("test-op-id"));

        String requestJson = """
            {
                "cardFromNumber": "1234567812345678",
                "cardFromValidTill": "12/25",
                "cardFromCVV": "123",
                "cardToNumber": "8765432187654321",
                "amount": {
                    "value": 1000,
                    "currency": "RUB"
                }
            }
            """;

        // When & Then
        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationId").value("test-op-id"));
    }

    @Test
    void confirm_ShouldReturnOk_WhenValidRequest() throws Exception {
        // Given
        when(transferService.confirm(any())).thenReturn(new TransferResponse("test-op-id"));

        String requestJson = """
            {
                "operationId": "test-op-id",
                "code": "0000"
            }
            """;

        // When & Then
        mockMvc.perform(post("/confirmOperation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationId").value("test-op-id"));
    }
}
