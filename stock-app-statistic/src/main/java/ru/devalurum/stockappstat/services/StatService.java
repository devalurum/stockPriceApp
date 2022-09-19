package ru.devalurum.stockappstat.services;

import ru.devalurum.stockappstat.domain.entity.StatEntity;

import java.util.Optional;

public interface StatService {

    void save(long countRequests);

    Optional<StatEntity> getStat();

    boolean exist(int id);
}
