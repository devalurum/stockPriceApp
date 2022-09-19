package ru.devalurum.telegramstockbot.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.devalurum.telegramstockbot.domain.dto.MessageShortDto;
import ru.devalurum.telegramstockbot.domain.dto.StatDto;
import ru.devalurum.telegramstockbot.domain.dto.StockDto;
import ru.devalurum.telegramstockbot.exception.KafkaException;
import ru.devalurum.telegramstockbot.telegrambot.TelegramBot;
import ru.devalurum.telegramstockbot.utils.Utils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static ru.devalurum.telegramstockbot.utils.Utils.statAsStringMessageForTelegram;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final TelegramBot telegramBot;
    private final ObjectMapper mapper;

    @KafkaListener(topics = "${kafka.stock.topic.name}",
            groupId = "${kafka.stock.topic.group.id}")
    public void consume(String stockDtoAsString) {
        MessageShortDto message;
        StockDto stockDto;
        try {
            message = mapper.readValue(stockDtoAsString, MessageShortDto.class);
            stockDto = mapper.readValue(message.getMessage(), StockDto.class);
        } catch (Exception ex) {
            log.error("can't parse message:{}", stockDtoAsString, ex);
            throw new KafkaException("can't parse message:" + stockDtoAsString, ex);
        }

        String stockMessage = Utils.stockAsStringMessageForTelegram(stockDto);
        telegramBot.sendResponse(message.getChatId(), stockMessage, message.getMessageId());
    }

    @KafkaListener(topics = "${kafka.log.topic.name}",
            groupId = "${kafka.log.topic.group.id}")
    public void consumeLog(String errorDto) {
        MessageShortDto message;
        try {
            message = mapper.readValue(errorDto, MessageShortDto.class);
        } catch (Exception ex) {
            log.error("can't parse message:{}", errorDto, ex);
            throw new KafkaException("can't parse message:" + errorDto, ex);
        }

        telegramBot.sendResponse(message.getChatId(), message.getMessage(), message.getMessageId());
    }

    @KafkaListener(topics = "${kafka.stat_answer.topic.name}",
            groupId = "${kafka.stat_answer.topic.group.id}")
    public void consumeStat(String requestStat) {
        MessageShortDto message;
        try {
            message = mapper.readValue(requestStat, MessageShortDto.class);
            StatDto stat = mapper.readValue(message.getMessage(), StatDto.class);
            telegramBot.sendResponse(message.getChatId(), statAsStringMessageForTelegram(stat), message.getMessageId());
        } catch (Exception ex) {
            log.error("can't parse message:{}", requestStat, ex);
            throw new KafkaException("can't parse message:" + requestStat, ex);
        }
    }
}
