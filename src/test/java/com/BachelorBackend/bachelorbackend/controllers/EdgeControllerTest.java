package com.BachelorBackend.bachelorbackend.controllers;

import com.BachelorBackend.bachelorbackend.services.EdgeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class EdgeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EdgeService edgeService;

    @Test
    void getEdgeData() throws Exception {
        this.mockMvc.perform(
                get("/getEdgeData?earliestDate=1234567&latestDate=23456789&filterService="))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getEdgeDataWithoutQueryParams() throws Exception {
        this.mockMvc.perform(
                get("/getEdgeData"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}