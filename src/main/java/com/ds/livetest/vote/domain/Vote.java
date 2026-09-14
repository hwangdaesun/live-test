package com.ds.livetest.vote.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "votes",
    uniqueConstraints = @UniqueConstraint(name = "uk_votes_voter_id", columnNames = "voter_id"),
    indexes = @Index(name = "idx_votes_choice", columnList = "choice"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;

  @Column(name = "voter_id", nullable = false, updatable = false, length = 100)
  private String voterId;

  @Convert(converter = VoteChoiceConverter.class)
  @Column(nullable = false, updatable = false, length = 20)
  private VoteChoice choice;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  private Vote(UUID id, String voterId, VoteChoice choice, LocalDateTime createdAt) {
    this.id = Objects.requireNonNull(id);
    this.voterId = requireVoterId(voterId);
    this.choice = Objects.requireNonNull(choice);
    this.createdAt = Objects.requireNonNull(createdAt);
  }

  public static Vote create(String voterId, VoteChoice choice) {
    return new Vote(UUID.randomUUID(), voterId, choice, LocalDateTime.now(ZoneOffset.UTC));
  }

  private static String requireVoterId(String voterId) {
    if (voterId == null || voterId.isBlank()) {
      throw new IllegalArgumentException("voterId must not be blank");
    }
    return voterId;
  }
}
