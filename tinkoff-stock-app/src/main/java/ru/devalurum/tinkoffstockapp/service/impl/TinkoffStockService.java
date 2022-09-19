package ru.devalurum.tinkoffstockapp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import ru.devalurum.tinkoffstockapp.domain.dto.StockDto;
import ru.devalurum.tinkoffstockapp.domain.mapper.StockMapper;
import ru.devalurum.tinkoffstockapp.domain.model.Stock;
import ru.devalurum.tinkoffstockapp.exception.StockNotFoundException;
import ru.devalurum.tinkoffstockapp.service.StockService;
import ru.devalurum.tinkoffstockapp.utils.Constants;
import ru.tinkoff.piapi.contract.v1.Instrument;
import ru.tinkoff.piapi.contract.v1.InstrumentShort;
import ru.tinkoff.piapi.contract.v1.LastPrice;
import ru.tinkoff.piapi.contract.v1.SecurityTradingStatus;
import ru.tinkoff.piapi.core.InstrumentsService;
import ru.tinkoff.piapi.core.InvestApi;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TinkoffStockService implements StockService {

    private final InvestApi investApi;
    private final StockMapper stockMapper;

    @Override
    public StockDto getStockByTicker(@NotNull String ticker) {
        var instrumentsService = investApi.getInstrumentsService();
        Instrument item = getInstrument(ticker, instrumentsService);

        LastPrice lastPrice = getLastPrice(item.getFigi());

        Stock stock = stockMapper.mapToModel(item, lastPrice, Constants.TINKOFF);
        return stockMapper.toDto(stock);
    }

    @Override
    public Instrument getInstumentByTicker(@NotNull String ticker) {
        var instrumentsService = investApi.getInstrumentsService();

        return getInstrument(ticker, instrumentsService);
    }

    @Override
    public SecurityTradingStatus getStatusByFigi(@NotNull String figi) {
        var marketDataService = investApi.getMarketDataService();
        return marketDataService.getTradingStatus(figi)
                .join()
                .getTradingStatus();
    }


    /***
     *  Если торговый статус НЕ в нормальном режиме трейда, то кэшируем по FIGI,
     *  иначе удаляем из кэша и запрашиваем каждый раз цену акции.
     ***/
    //ToDo: Переделать кэширование с соотвествием расписания открытия и закрытия бирж.
    @Override
    @Caching(
            cacheable = {@Cacheable(cacheNames = "stock", key = "#item.figi",
condition = "#status != T(ru.tinkoff.piapi.contract.v1.SecurityTradingStatus).SECURITY_TRADING_STATUS_NORMAL_TRADING")},
            evict = {@CacheEvict(cacheNames = "stock", key = "#item.figi",
condition = "#status == T(ru.tinkoff.piapi.contract.v1.SecurityTradingStatus).SECURITY_TRADING_STATUS_NORMAL_TRADING")}
    )
    public StockDto getStock(Instrument item, SecurityTradingStatus status) {
        LastPrice lastPrice = getLastPrice(item.getFigi());

        Stock stock = stockMapper.mapToModel(item, lastPrice, Constants.TINKOFF);
        log.info("Запрос цены по тикеру {} к Tinkoff", item.getTicker());
        return stockMapper.toDto(stock);
    }


    private LastPrice getLastPrice(String figi) {
        var marketDataService = investApi.getMarketDataService();

        var lastPrices = marketDataService.getLastPrices(List.of(figi));
        return lastPrices.join().stream()
                .findFirst()
                .orElseThrow(() -> new StockNotFoundException(String.format("Last Price by '%S' not found.", figi)));
    }

    private Instrument getInstrument(String ticker, InstrumentsService instrumentsService) {
        var instrument = instrumentsService.findInstrument(ticker);

        List<InstrumentShort> list = instrument.join();

        InstrumentShort itemShort = list.stream()
                .filter(is -> is.getTicker().equalsIgnoreCase(ticker))
                .findFirst()
                .orElseThrow(() -> new StockNotFoundException(String.format("Stock '%S' not found.", ticker)));

        var cfInst = instrumentsService.getInstrumentByTicker(itemShort.getTicker(),
                itemShort.getClassCode());

        return cfInst.join();
    }

}
