package com.charter.rewards.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
class RewardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnRewardsForMultipleCustomers() throws Exception {
        mockMvc.perform(get("/api/rewards")
                        .param("from", "2026-05-01")
                        .param("to", "2026-07-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Alice Johnson"))
                .andExpect(jsonPath("$[0].monthlyRewards[0].month").value("2026-05"))
                .andExpect(jsonPath("$[0].monthlyRewards[0].points").value(115))
                .andExpect(jsonPath("$[0].monthlyRewards[1].month").value("2026-06"))
                .andExpect(jsonPath("$[0].monthlyRewards[1].points").value(270))
                .andExpect(jsonPath("$[0].monthlyRewards[2].month").value("2026-07"))
                .andExpect(jsonPath("$[0].monthlyRewards[2].points").value(170))
                .andExpect(jsonPath("$[0].totalPoints").value(555))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnBadRequestWhenDateRangeIsReversed() throws Exception {
        mockMvc.perform(get("/api/rewards")
                        .param("from", "2026-07-31")
                        .param("to", "2026-05-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("The from date must not be after the to date."));
    }

    @Test
    void shouldReturnBadRequestWhenRequiredDateIsMissing() throws Exception {
        mockMvc.perform(get("/api/rewards")
                        .param("from", "2026-05-01"))
                .andExpect(status().isBadRequest());
    }
}
