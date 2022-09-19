package ru.devalurum.telegramstockbot.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PointReaderException extends RuntimeException {

    public PointReaderException(Exception e) {
        super(e);
    }

    public PointReaderException(String message) {
        super(message);
    }

    public PointReaderException(String message, Exception e) {
        super(message, e);
    }
}
