package ru.devalurum.telegramstockbot.telegrambot.strategy.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.devalurum.telegramstockbot.service.kafka.KafkaProducerService;
import ru.devalurum.telegramstockbot.telegrambot.TelegramBot;
import ru.devalurum.telegramstockbot.telegrambot.strategy.TelegramAnswerStrategy;
import ru.devalurum.telegramstockbot.telegrambot.strategy.TelegramCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramAnswerStat implements TelegramAnswerStrategy {

    private final KafkaProducerService producerService;

    @Override
    public void sendAnswer(Message message, TelegramBot telegramBot) {
        producerService.sendRequestStatMessage(message);
    }

    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.BOT_STAT;
    }
}
