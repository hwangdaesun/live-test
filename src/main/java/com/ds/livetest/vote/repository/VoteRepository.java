package com.ds.livetest.vote.repository;

import com.ds.livetest.vote.domain.Vote;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface VoteRepository extends JpaRepository<Vote, UUID> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Vote> findByVoterId(String voterId);
}
