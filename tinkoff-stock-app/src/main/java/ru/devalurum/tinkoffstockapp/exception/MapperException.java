package ru.devalurum.tinkoffstockapp.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MapperException extends RuntimeException {

    public MapperException(String message) {
        super(message);
    }

    public MapperException(String message, Exception ex) {
        super(message, ex);
    }

}
