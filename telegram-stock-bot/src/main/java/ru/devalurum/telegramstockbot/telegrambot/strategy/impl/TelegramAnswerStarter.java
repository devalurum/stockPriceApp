package ru.devalurum.telegramstockbot.telegrambot.strategy.impl;

import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.devalurum.telegramstockbot.domain.dto.UserDto;
import ru.devalurum.telegramstockbot.domain.mapper.UserMapper;
import ru.devalurum.telegramstockbot.service.UserService;
import ru.devalurum.telegramstockbot.telegrambot.TelegramBot;
import ru.devalurum.telegramstockbot.telegrambot.strategy.TelegramCommand;
import ru.devalurum.telegramstockbot.telegrambot.strategy.TelegramAnswerStrategy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.devalurum.telegramstockbot.utils.Constants;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramAnswerStarter implements TelegramAnswerStrategy {

    private final UserService userService;
    private final UserMapper mapper;

    @Override
    public void sendAnswer(Message message, TelegramBot telegramBot) {

        long chatId = message.getChatId();

        String user;

        if (message.getChat().getFirstName() != null) {
            user = message.getChat().getFirstName();
        } else {
            user = message.getChat().getUserName();
        }

        final String answer = EmojiParser.parseToUnicode(String.format(Constants.ANSWER_AFTER_START, user));

        registerUser(message.getChat());

        telegramBot.sendResponseWithLocation(chatId, answer);
    }

    private void registerUser(Chat chat) {
        userService.existById(chat.getId())
                .filter(isTrue -> isTrue)
                .ifPresentOrElse(
                        v -> log.info("'{}' already exist in database", chat.getUserName())
                        , () -> {
                            UserDto user = mapper.createDto(chat, LocalDateTime.now(ZoneOffset.UTC));

                            Optional.ofNullable(chat.getLocation())
                                    .ifPresent(loc -> user.setCoordinates(loc.getLocation()));

                            userService.save(user);

                            log.info("User saved: {}", user);
                        });
    }

    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.START;
    }
}
