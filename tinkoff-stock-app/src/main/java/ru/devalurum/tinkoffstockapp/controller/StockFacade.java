package ru.devalurum.tinkoffstockapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.devalurum.tinkoffstockapp.domain.dto.StockDto;
import ru.devalurum.tinkoffstockapp.service.StockService;
import ru.tinkoff.piapi.contract.v1.Instrument;
import ru.tinkoff.piapi.contract.v1.SecurityTradingStatus;

import javax.validation.constraints.NotNull;

@Component
@RequiredArgsConstructor
public class StockFacade {

    private final StockService stockService;

    public StockDto getStock(@NotNull String ticker) {
        Instrument item = stockService.getInstumentByTicker(ticker);
        SecurityTradingStatus status = stockService.getStatusByFigi(item.getFigi());

        return stockService.getStock(item, status);
    }

}
