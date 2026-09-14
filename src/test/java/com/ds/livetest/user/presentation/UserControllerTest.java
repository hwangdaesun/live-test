package com.ds.livetest.user.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ds.livetest.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class UserControllerTest extends IntegrationTestSupport {

  @Autowired private MockMvc mockMvc;

  @Test
  void createUserSavesAndReturnsUserResponse() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "email": "user@example.com",
                      "nickname": "live-user"
                    }
                    """))
        .andExpect(status().isCreated())
        .andExpect(header().exists(HttpHeaders.LOCATION))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").isString())
        .andExpect(jsonPath("$.data.email").value("user@example.com"))
        .andExpect(jsonPath("$.data.nickname").value("live-user"))
        .andExpect(jsonPath("$.error").doesNotExist());
  }

  @Test
  void createUserReturnsConflictWhenEmailAlreadyExists() throws Exception {
    createUser("user@example.com", "live-user");

    mockMvc
        .perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "email": "user@example.com",
                      "nickname": "other-user"
                    }
                    """))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.data").doesNotExist())
        .andExpect(jsonPath("$.error.code").value("USER-001"))
        .andExpect(jsonPath("$.error.message").value("이미 사용 중인 이메일입니다."))
        .andExpect(jsonPath("$.error.timestamp").exists());
  }

  @Test
  void createUserReturnsBadRequestWhenRequestIsInvalid() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "email": "not-email",
                      "nickname": ""
                    }
                    """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.data").doesNotExist())
        .andExpect(jsonPath("$.error.code").value("COMMON-001"))
        .andExpect(jsonPath("$.error.fieldErrors").isArray())
        .andExpect(jsonPath("$.error.timestamp").exists());
  }

  private void createUser(String email, String nickname) throws Exception {
    mockMvc
        .perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "email": "%s",
                      "nickname": "%s"
                    }
                    """
                        .formatted(email, nickname)))
        .andExpect(status().isCreated());
  }
}
