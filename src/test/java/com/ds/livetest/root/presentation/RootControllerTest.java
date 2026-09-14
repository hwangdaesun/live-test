package com.ds.livetest.root.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ds.livetest.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class RootControllerTest extends IntegrationTestSupport {

  @Autowired private MockMvc mockMvc;

  @Test
  void getRootReturnsOk() throws Exception {
    mockMvc
        .perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data").value("ok"))
        .andExpect(jsonPath("$.error").doesNotExist());
  }

  @Test
  void getHealthReturnsOk() throws Exception {
    mockMvc
        .perform(get("/health"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data").value("ok"))
        .andExpect(jsonPath("$.error").doesNotExist());
  }

  @Test
  void unknownEndpointReturnsNotFoundErrorResponse() throws Exception {
    mockMvc
        .perform(get("/unknown"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.data").doesNotExist())
        .andExpect(jsonPath("$.error.code").value("COMMON-002"))
        .andExpect(jsonPath("$.error.message").value("요청한 리소스를 찾을 수 없습니다."))
        .andExpect(jsonPath("$.error.fieldErrors").isArray())
        .andExpect(jsonPath("$.error.timestamp").exists());
  }
}
