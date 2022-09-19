package ru.devalurum.telegramstockbot.telegrambot.strategy.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.devalurum.telegramstockbot.telegrambot.TelegramBot;
import ru.devalurum.telegramstockbot.telegrambot.strategy.TelegramAnswerStrategy;
import ru.devalurum.telegramstockbot.telegrambot.strategy.TelegramCommand;
import ru.devalurum.telegramstockbot.utils.Utils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramAnswerTechStack implements TelegramAnswerStrategy {

    private static final String TECH_STACK = "Java 11, Spring Boot, Spring Data JPA/JDBC, Spring Kafka," +
            "Spring Cache (Caffeine), Dotenv for Spring (support .env files), Lombok, TelegramBots (Spring Boot Starter)," +
            "MapStruct, Java SDK for Tinkoff Invest API, Gradle, Docker, PostgreSQL (+PostGIS), Flyway," +
            "Swagger (OpenAPI 3.0), Kubernetes (Kind), GitLab CI";

    private static final String MORE =
            "Резюме на [Habr.Career](https://career.habr.com/devalurum). \n"
                    + "Исходный код на [Github](https://github.com/devalurum). \n"
                    + "Контакт [разработчика](t.me/devalurum).";

    @Override
    public void sendAnswer(Message message, TelegramBot telegramBot) {
        long chatId = message.getChatId();
        String techStack = Utils.convertStrBySplitWithCommaToUnmarkedList(TECH_STACK);
        techStack += "\n\n" + MORE;

        telegramBot.sendResponse(chatId, techStack, ParseMode.MARKDOWN);
    }

    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.TECH_STACK;
    }
}
