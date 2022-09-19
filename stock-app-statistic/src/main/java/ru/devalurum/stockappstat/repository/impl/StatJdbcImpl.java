package ru.devalurum.stockappstat.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.devalurum.stockappstat.domain.entity.StatEntity;
import ru.devalurum.stockappstat.exception.JdbcRepositoryException;
import ru.devalurum.stockappstat.repository.StatJdbc;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StatJdbcImpl implements StatJdbc {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private final static String FIND = "SELECT * " +
            "FROM stat AS u " +
            "WHERE id=:id";

    private final static String IF_EXIST = "SELECT count(*) as N " +
            "FROM stat " +
            "WHERE id=:id";

    private final static String SAVE = "INSERT INTO stat (id, last_update, count_requests, start_date) " +
            "VALUES (1, :update_time, :count_requests, :start_date)";

    private final static String UPDATE = "UPDATE stat " +
            "SET last_update=:update_time, count_requests=:count_requests " +
            "WHERE id=:id";


    @Override
    public StatEntity findStat() {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("id", 1);

            return namedJdbcTemplate.queryForObject(FIND, parameters, (rs, rowNum) -> StatEntity.builder()
                    .startTime(rs.getTimestamp("start_date").toLocalDateTime())
                    .updateTime(rs.getTimestamp("last_update").toLocalDateTime())
                    .countRequests(rs.getLong("count_requests"))
                    .build());
        } catch (EmptyResultDataAccessException ex) {
            throw new JdbcRepositoryException("Stat not found.", ex);
        }
    }


    @Override
    public boolean ifExists(int id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        Integer count = namedJdbcTemplate.queryForObject(IF_EXIST, parameters, Integer.class);

        return Optional.ofNullable(count)
                .map(c -> c > 0)
                .orElse(false);
    }

    @Override
    public int save(long countRequests) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("start_date", LocalDateTime.now(ZoneOffset.UTC))
                .addValue("update_time", LocalDateTime.now(ZoneOffset.UTC))
                .addValue("count_requests", countRequests);

        return namedJdbcTemplate.update(SAVE, parameters);
    }

    @Override
    public int update(StatEntity statEntity) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", statEntity.getId())
                .addValue("update_time", statEntity.getUpdateTime())
                .addValue("count_requests", statEntity.getCountRequests());

        return namedJdbcTemplate.update(UPDATE, parameters);
    }

    @Override
    public void upsert(StatEntity statEntity) {
        if (ifExists(statEntity.getId())) {
            update(statEntity);
        } else {
            save(statEntity.getCountRequests());
        }
    }
}
