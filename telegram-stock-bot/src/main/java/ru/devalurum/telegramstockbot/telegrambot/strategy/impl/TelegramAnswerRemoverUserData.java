package ru.devalurum.telegramstockbot.telegrambot.strategy.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.devalurum.telegramstockbot.service.UserService;
import ru.devalurum.telegramstockbot.telegrambot.TelegramBot;
import ru.devalurum.telegramstockbot.telegrambot.strategy.TelegramAnswerStrategy;
import ru.devalurum.telegramstockbot.telegrambot.strategy.TelegramCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import static ru.devalurum.telegramstockbot.utils.Constants.DELETE_INFO;
import static ru.devalurum.telegramstockbot.utils.Constants.INFO_NOT_STORED;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramAnswerRemoverUserData implements TelegramAnswerStrategy {

    private final UserService userService;

    @Override
    public void sendAnswer(Message message, TelegramBot telegramBot) {

        long chatId = message.getChatId();

        userService.existById(chatId)
                .filter(isTrue -> isTrue)
                .ifPresentOrElse(
                        v -> {
                            userService.deleteById(chatId);
                            telegramBot.sendResponse(chatId, DELETE_INFO);
                        }, () -> telegramBot.sendResponse(chatId, INFO_NOT_STORED));
    }


    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.DELETE_DATA;
    }
}
