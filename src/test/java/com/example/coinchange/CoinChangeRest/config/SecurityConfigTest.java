package com.example.coinchange.CoinChangeRest.config;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testSwaggerUiEndpointWithAuthentication() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html")
                        .with(httpBasic("doguscan", "doguscan")))
                .andExpect(status().isOk());
    }

    @Test
    public void testApiDocsEndpointWithAuthentication() throws Exception {
        mockMvc.perform(get("/v3/api-docs")
                        .with(httpBasic("doguscan", "doguscan")))
                .andExpect(status().isOk());
    }

    @Test
    public void testHealthEndpointAccess() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }
}

