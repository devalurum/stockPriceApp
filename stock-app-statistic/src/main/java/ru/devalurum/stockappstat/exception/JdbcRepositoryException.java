package ru.devalurum.stockappstat.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JdbcRepositoryException extends RuntimeException {

    public JdbcRepositoryException(String message) {
        super(message);
    }

    public JdbcRepositoryException(String message, Exception ex) {
        super(message, ex);
    }

}