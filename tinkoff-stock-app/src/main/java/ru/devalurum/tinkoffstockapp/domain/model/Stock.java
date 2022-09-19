package ru.devalurum.tinkoffstockapp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@AllArgsConstructor
public class Stock {

    String ticker;
    String figi;
    String name;
    Currency currency;
    Exchange exchange;
    String source;

    BigDecimal price;
    LocalDateTime time;

}
