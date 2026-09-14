package com.ds.livetest.vote.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vote_statistics")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteStatistic {

  @Id
  @Column(nullable = false, updatable = false, length = 20)
  private String choice;

  @Column(name = "vote_count", nullable = false)
  private long voteCount;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  private VoteStatistic(String choice, long voteCount, LocalDateTime updatedAt) {
    this.choice = Objects.requireNonNull(choice);
    this.voteCount = requireNonNegative(voteCount);
    this.updatedAt = Objects.requireNonNull(updatedAt);
  }

  public static VoteStatistic initialize(VoteChoice choice) {
    return new VoteStatistic(choice.value(), 0, LocalDateTime.now(ZoneOffset.UTC));
  }

  public void updateCount(long voteCount) {
    this.voteCount = requireNonNegative(voteCount);
    updatedAt = LocalDateTime.now(ZoneOffset.UTC);
  }

  public VoteChoice getChoice() {
    return VoteChoice.from(choice);
  }

  public long getVoteCount() {
    return voteCount;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  private static long requireNonNegative(long voteCount) {
    if (voteCount < 0) {
      throw new IllegalArgumentException("voteCount must not be negative");
    }
    return voteCount;
  }
}
