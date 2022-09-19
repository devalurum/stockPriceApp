package ru.devalurum.tinkoffstockapp.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.devalurum.tinkoffstockapp.utils.Utils;

@RequiredArgsConstructor
@Getter
public enum Exchange {
    MOEX("MOEX", "Московская биржа (MOEX)"),
    SPBE("SPB", "Cанкт-Петербургская биржа (SPBE)");
    private final String exchangeAsString;
    private final String description;

    public static Exchange getExchangeByText(String text) {
        for (Exchange exchange : values()) {
            if (exchange.getExchangeAsString().equalsIgnoreCase(text) ||
                    text.contains(exchange.getExchangeAsString()) ||
                    Utils.containsIgnoreCase(text, exchange.getExchangeAsString())) {
                return exchange;
            }
        }

        throw new IllegalArgumentException(String.format("No enum found with this text: %s", text));
    }
}
