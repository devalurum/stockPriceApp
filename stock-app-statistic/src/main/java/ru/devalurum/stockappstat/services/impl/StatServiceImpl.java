package ru.devalurum.stockappstat.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.devalurum.stockappstat.domain.entity.StatEntity;
import ru.devalurum.stockappstat.exception.JdbcRepositoryException;
import ru.devalurum.stockappstat.repository.StatJdbc;
import ru.devalurum.stockappstat.services.StatService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatServiceImpl implements StatService {

    private final StatJdbc repository;

    @Override
    public void save(long count) {
        StatEntity entity = StatEntity.builder()
                .id(1)
                .countRequests(count)
                .updateTime(LocalDateTime.now(ZoneOffset.UTC))
                .build();
        repository.upsert(entity);
    }

    @Override
    public Optional<StatEntity> getStat() {
        try {
            return Optional.ofNullable(repository.findStat());
        } catch (JdbcRepositoryException ex) {
            return Optional.empty();
        }
    }

    @Override
    public boolean exist(int id) {
        return repository.ifExists(id);
    }
}
