package ru.devalurum.telegramstockbot.telegrambot.strategy.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.devalurum.telegramstockbot.telegrambot.TelegramBot;
import ru.devalurum.telegramstockbot.telegrambot.strategy.TelegramAnswerStrategy;
import ru.devalurum.telegramstockbot.telegrambot.strategy.TelegramCommand;
import ru.devalurum.telegramstockbot.utils.Constants;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramAnswerNotFoundCommand implements TelegramAnswerStrategy {

    @Override
    public void sendAnswer(Message message, TelegramBot telegramBot) {

        long chatId = message.getChatId();

        telegramBot.sendResponse(chatId, Constants.WRONG_REQUEST);
    }

    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.NOT_FOUND;
    }
}
