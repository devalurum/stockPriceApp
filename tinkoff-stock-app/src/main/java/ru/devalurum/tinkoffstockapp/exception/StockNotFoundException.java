package ru.devalurum.tinkoffstockapp.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StockNotFoundException extends RuntimeException {

    public StockNotFoundException(String message) {
        super(message);
    }

}
