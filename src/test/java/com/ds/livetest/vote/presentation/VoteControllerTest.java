package com.ds.livetest.vote.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ds.livetest.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class VoteControllerTest extends IntegrationTestSupport {

  @Autowired private MockMvc mockMvc;

  @Test
  void createVoteSavesAndReturnsVoteResponse() throws Exception {
    mockMvc
        .perform(
            post("/api/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "choice": "jajang",
                      "voterId": "user-123"
                    }
                    """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").isString())
        .andExpect(jsonPath("$.data.choice").value("jajang"))
        .andExpect(jsonPath("$.data.voterId").value("user-123"))
        .andExpect(jsonPath("$.error").doesNotExist());
  }

  @Test
  void getVoteResultReturnsZeroCountsWhenStatisticsAreEmpty() throws Exception {
    mockMvc
        .perform(get("/api/result"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.jajang").value(0))
        .andExpect(jsonPath("$.data.jjamppong").value(0))
        .andExpect(jsonPath("$.data.total").value(0))
        .andExpect(jsonPath("$.error").doesNotExist());
  }

  @Test
  void getVoteResultReturnsCurrentStatisticsAfterSuccessfulVotes() throws Exception {
    createVote("jajang", "user-1");
    createVote("jajang", "user-2");
    createVote("jjamppong", "user-3");

    mockMvc
        .perform(get("/api/result"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.jajang").value(2))
        .andExpect(jsonPath("$.data.jjamppong").value(1))
        .andExpect(jsonPath("$.data.total").value(3))
        .andExpect(jsonPath("$.error").doesNotExist());
  }

  @Test
  void createVoteReturnsConflictWhenVoterAlreadyVoted() throws Exception {
    createVote("jajang", "user-123");

    mockMvc
        .perform(
            post("/api/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "choice": "jjamppong",
                      "voterId": "user-123"
                    }
                    """))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.data").doesNotExist())
        .andExpect(jsonPath("$.error.code").value("VOTE-001"))
        .andExpect(jsonPath("$.error.message").value("이미 투표한 사용자입니다."))
        .andExpect(jsonPath("$.error.timestamp").exists());
  }

  @Test
  void createVoteReturnsBadRequestWhenChoiceIsInvalid() throws Exception {
    mockMvc
        .perform(
            post("/api/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "choice": "ramen",
                      "voterId": "user-123"
                    }
                    """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.data").doesNotExist())
        .andExpect(jsonPath("$.error.code").value("COMMON-001"))
        .andExpect(jsonPath("$.error.timestamp").exists());
  }

  @Test
  void createVoteReturnsBadRequestWhenRequiredValuesAreMissing() throws Exception {
    mockMvc
        .perform(
            post("/api/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                    }
                    """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.data").doesNotExist())
        .andExpect(jsonPath("$.error.code").value("COMMON-001"))
        .andExpect(jsonPath("$.error.fieldErrors").isArray())
        .andExpect(jsonPath("$.error.timestamp").exists());
  }

  @Test
  void createVoteReturnsBadRequestWhenVoterIdIsBlank() throws Exception {
    mockMvc
        .perform(
            post("/api/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "choice": "jajang",
                      "voterId": "   "
                    }
                    """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.data").doesNotExist())
        .andExpect(jsonPath("$.error.code").value("COMMON-001"))
        .andExpect(jsonPath("$.error.fieldErrors").isArray())
        .andExpect(jsonPath("$.error.timestamp").exists());
  }

  @Test
  void createVoteReturnsBadRequestWhenVoterIdIsTooLong() throws Exception {
    mockMvc
        .perform(
            post("/api/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "choice": "jajang",
                      "voterId": "%s"
                    }
                    """
                        .formatted("a".repeat(101))))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.data").doesNotExist())
        .andExpect(jsonPath("$.error.code").value("COMMON-001"))
        .andExpect(jsonPath("$.error.fieldErrors").isArray())
        .andExpect(jsonPath("$.error.timestamp").exists());
  }

  private void createVote(String choice, String voterId) throws Exception {
    mockMvc
        .perform(
            post("/api/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "choice": "%s",
                      "voterId": "%s"
                    }
                    """
                        .formatted(choice, voterId)))
        .andExpect(status().isCreated());
  }
}
