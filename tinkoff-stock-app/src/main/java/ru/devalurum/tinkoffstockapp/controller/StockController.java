package ru.devalurum.tinkoffstockapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.devalurum.tinkoffstockapp.domain.dto.ErrorDto;
import ru.devalurum.tinkoffstockapp.domain.dto.StockDto;
import ru.devalurum.tinkoffstockapp.service.StockService;
import ru.tinkoff.piapi.contract.v1.Instrument;
import ru.tinkoff.piapi.contract.v1.SecurityTradingStatus;

@RequiredArgsConstructor
@RestController
@Tag(name = "Stocks")
@RequestMapping("/api/v1/stocks")
@Slf4j
public class StockController {

    private final StockFacade stockFacade;

    @Operation(
            summary = "Get stock by ticker",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Stock for requested ticker",
                            content = @Content(schema = @Schema(implementation = StockDto.class))),
                    @ApiResponse(responseCode = "404", description = "Requested data not found",
                            content = @Content(schema = @Schema(implementation = ErrorDto.class)))
            }
    )
    @GetMapping(value = "/{ticker}", produces = MediaType.APPLICATION_JSON_VALUE)
    public StockDto getStock(@PathVariable String ticker) {
        return stockFacade.getStock(ticker);
    }

}
