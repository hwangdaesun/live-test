package com.ds.livetest.vote.repository;

import com.ds.livetest.vote.domain.VoteStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoteStatisticRepository extends JpaRepository<VoteStatistic, String> {

  @Modifying
  @Query(
      value =
          """
          INSERT INTO vote_statistics (choice, vote_count, updated_at)
          VALUES (:choice, 1, CURRENT_TIMESTAMP(6))
          ON DUPLICATE KEY UPDATE
            vote_count = vote_count + 1,
            updated_at = CURRENT_TIMESTAMP(6)
          """,
      nativeQuery = true)
  void increase(@Param("choice") String choice);
}
