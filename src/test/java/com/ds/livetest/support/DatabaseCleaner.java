package com.ds.livetest.support;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseCleaner {

  private static final List<String> EXCLUDED_TABLES =
      List.of("flyway_schema_history", "databasechangelog", "databasechangeloglock");

  private final JdbcTemplate jdbcTemplate;

  public void clean() {
    List<String> tableNames =
        jdbcTemplate.queryForList(
            """
            SELECT TABLE_NAME
            FROM INFORMATION_SCHEMA.TABLES
            WHERE TABLE_SCHEMA = DATABASE()
              AND TABLE_TYPE = 'BASE TABLE'
            """,
            String.class);

    if (tableNames.isEmpty()) {
      return;
    }

    jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
    try {
      tableNames.stream()
          .filter(tableName -> !EXCLUDED_TABLES.contains(tableName))
          .forEach(tableName -> jdbcTemplate.execute("TRUNCATE TABLE `" + tableName + "`"));
    } finally {
      jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }
  }
}
