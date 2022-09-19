package ru.devalurum.stockappstat.repository;

import ru.devalurum.stockappstat.domain.entity.StatEntity;

public interface StatJdbc {

    StatEntity findStat();

    boolean ifExists(int id);

    int save(long countRequests);

    int update(StatEntity statEntity);

    void upsert(StatEntity statEntity);

}
