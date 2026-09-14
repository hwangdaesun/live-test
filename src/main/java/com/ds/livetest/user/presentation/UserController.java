package com.ds.livetest.user.presentation;

import com.ds.livetest.generated.dto.CreateUserRequest;
import com.ds.livetest.support.response.ApiEnvelope;
import com.ds.livetest.user.domain.User;
import com.ds.livetest.user.service.UserService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping(
      value = "/api/v1/users",
      consumes = "application/json",
      produces = "application/json")
  public ResponseEntity<ApiEnvelope> createUser(
      @Valid @RequestBody CreateUserRequest createUserRequest) {
    User user =
        userService.createUser(createUserRequest.getEmail(), createUserRequest.getNickname());

    return ResponseEntity.created(URI.create("/api/v1/users/" + user.getId()))
        .body(ApiEnvelope.ofSuccess(UserResponse.from(user)));
  }
}
