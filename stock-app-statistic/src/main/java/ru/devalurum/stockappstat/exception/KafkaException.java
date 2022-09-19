package ru.devalurum.stockappstat.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class KafkaException extends RuntimeException {

    public KafkaException(String message) {
        super(message);
    }

    public KafkaException(String message, Exception ex) {
        super(message, ex);
    }

}
