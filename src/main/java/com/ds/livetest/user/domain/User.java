package com.ds.livetest.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;

  @Column(nullable = false, unique = true, length = 255)
  private String email;

  @Column(nullable = false, unique = true, length = 50)
  private String nickname;

  private User(UUID id, String email, String nickname) {
    this.id = id;
    this.email = email;
    this.nickname = nickname;
  }

  public static User create(String email, String nickname) {
    return new User(UUID.randomUUID(), email, nickname);
  }
}
