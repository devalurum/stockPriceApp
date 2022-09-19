package ru.devalurum.tinkoffstockapp.domain.mapper;

import com.google.protobuf.Timestamp;
import org.mapstruct.*;
import ru.devalurum.tinkoffstockapp.domain.dto.StockDto;
import ru.devalurum.tinkoffstockapp.domain.model.Currency;
import ru.devalurum.tinkoffstockapp.domain.model.Exchange;
import ru.devalurum.tinkoffstockapp.domain.model.Stock;
import ru.tinkoff.piapi.contract.v1.Instrument;
import ru.tinkoff.piapi.contract.v1.LastPrice;
import ru.tinkoff.piapi.contract.v1.Quotation;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StockMapper {

    @Mapping(target = "figi", source = "item.figi")
    @Mapping(target = "ticker", source = "item.ticker")
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "currency", source = "item.currency", qualifiedByName = "strCurrencyToEnum")
    @Mapping(target = "exchange", source = "item.exchange", qualifiedByName = "strExchangeToEnum")
    @Mapping(target = "source", source = "source")
    @Mapping(target = "price", source = "lastPrice.price", qualifiedByName = "strToBigDecimal")
    @Mapping(target = "time", source = "lastPrice.time", qualifiedByName = "timestampToLocalDateTime")
    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    Stock mapToModel(Instrument item, LastPrice lastPrice, String source);

    @Mapping(target = "exchange", source = "exchange", qualifiedByName = "exchangeToStr")
    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    StockDto toDto(Stock stock);

    @Mapping(target = "exchange", source = "exchange", qualifiedByName = "strExchangeToEnum")
    @Mapping(source = "currency", target = "currency", qualifiedByName = "strCurrencyToEnum")
    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    Stock toModel(StockDto stockDto);


    @Named("strCurrencyToEnum")
    default Currency strCurrencyToEnum(@NotNull String currency) {
        return Currency.valueOf(currency.toUpperCase());
    }


    @Named("strExchangeToEnum")
    default Exchange strExchangeToEnum(@NotNull String exchange) {
        return Exchange.getExchangeByText(exchange);
    }

    @Named("exchangeToStr")
    default String exchangeToStr(@NotNull Exchange exchange) {
        return exchange.getDescription();
    }

    @Named("timestampToLocalDateTime")
    default LocalDateTime timestampToLocalDateTime(@NotNull Timestamp time) {
        return LocalDateTime.ofEpochSecond(time.getSeconds(), time.getNanos(), ZoneOffset.UTC);
    }

    @Named("strToBigDecimal")
    default BigDecimal strToBigDecimal(@NotNull Quotation quotation) {
        return new BigDecimal(String.format("%s.%s", quotation.getUnits(), quotation.getNano()));
    }

}
