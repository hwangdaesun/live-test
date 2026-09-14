package com.ds.livetest.user.service;

import com.ds.livetest.support.error.ConflictException;
import com.ds.livetest.user.UserErrorCode;
import com.ds.livetest.user.domain.User;
import com.ds.livetest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public User createUser(String email, String nickname) {
    if (userRepository.existsByEmail(email)) {
      throw new ConflictException(UserErrorCode.DUPLICATE_EMAIL);
    }
    if (userRepository.existsByNickname(nickname)) {
      throw new ConflictException(UserErrorCode.DUPLICATE_NICKNAME);
    }

    return userRepository.save(User.create(email, nickname));
  }
}
