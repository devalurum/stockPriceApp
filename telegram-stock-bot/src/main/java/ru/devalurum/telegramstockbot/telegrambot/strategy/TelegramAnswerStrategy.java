package ru.devalurum.telegramstockbot.telegrambot.strategy;

import ru.devalurum.telegramstockbot.telegrambot.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface TelegramAnswerStrategy {

    void sendAnswer(Message message, TelegramBot telegramBot);

    TelegramCommand getCommand();
}
