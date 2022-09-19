package ru.devalurum.telegramstockbot.config;


import ru.devalurum.telegramstockbot.telegrambot.strategy.TelegramAnswerStrategy;
import ru.devalurum.telegramstockbot.telegrambot.strategy.TelegramCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class TelegramStrategyConfig {

    @Bean
    public Map<TelegramCommand, TelegramAnswerStrategy> commandTelegramAnswerStrategyMap(List<TelegramAnswerStrategy> telegramAnswerStrategies) {
        return telegramAnswerStrategies
                .stream()
                .collect(Collectors.toMap(TelegramAnswerStrategy::getCommand, Function.identity()));
    }
}