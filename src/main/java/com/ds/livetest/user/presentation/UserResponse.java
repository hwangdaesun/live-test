package com.ds.livetest.user.presentation;

import com.ds.livetest.user.domain.User;
import java.util.UUID;

public record UserResponse(UUID id, String email, String nickname) {

  public static UserResponse from(User user) {
    return new UserResponse(user.getId(), user.getEmail(), user.getNickname());
  }
}
