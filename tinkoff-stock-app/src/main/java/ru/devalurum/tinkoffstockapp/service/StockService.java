package ru.devalurum.tinkoffstockapp.service;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import ru.devalurum.tinkoffstockapp.domain.dto.StockDto;
import ru.tinkoff.piapi.contract.v1.Instrument;
import ru.tinkoff.piapi.contract.v1.SecurityTradingStatus;

import javax.validation.constraints.NotNull;

public interface StockService {

    StockDto getStockByTicker(@NotNull String ticker);

    Instrument getInstumentByTicker(@NotNull String ticker);

    SecurityTradingStatus getStatusByFigi(@NotNull String figi);

    StockDto getStock(Instrument item, SecurityTradingStatus status);
}
