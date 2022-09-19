package ru.devalurum.telegramstockbot.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TelegramBotException extends RuntimeException {

    public TelegramBotException(Exception e) {
        super(e);
    }

    public TelegramBotException(String message) {
        super(message);
    }

    public TelegramBotException(String message, Exception e) {
        super(message, e);
    }

}
