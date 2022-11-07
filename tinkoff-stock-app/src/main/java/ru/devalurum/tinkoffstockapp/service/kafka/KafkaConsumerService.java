package ru.devalurum.tinkoffstockapp.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.devalurum.tinkoffstockapp.controller.StockFacade;
import ru.devalurum.tinkoffstockapp.domain.dto.MessageShortDto;
import ru.devalurum.tinkoffstockapp.domain.dto.StockDto;
import ru.devalurum.tinkoffstockapp.domain.mapper.MessageShortMapper;
import ru.devalurum.tinkoffstockapp.exception.StockNotFoundException;
import ru.tinkoff.piapi.core.exception.ApiRuntimeException;

import javax.annotation.Nonnull;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final StockFacade stockFacade;
    private final KafkaProducerService kafkaProducerService;
    private final KafkaErrorProducerService errorProducerService;
    private final ObjectMapper mapper;
    private final MessageShortMapper messageShortMapper;

    @KafkaListener(topics = "${kafka.general.topic.name}",
            groupId = "${kafka.general.topic.group.id}")
    public void consume(String message) {
        MessageShortDto messageShortDto = null;

        try {
            messageShortDto = mapper.readValue(message, MessageShortDto.class);
            log.info("Message recieved -> {}", messageShortDto.getMessage());

            StockDto stockDto = stockFacade.getStock(messageShortDto.getMessage());
            MessageShortDto messageToSendWithStock = messageShortMapper.createDto(messageShortDto.getChatId(),
                    messageShortDto.getMessageId(), stockDto);

            kafkaProducerService.sendStock(messageToSendWithStock);
        } catch (StockNotFoundException | ApiRuntimeException ex) {
            sendErrorToKafkaWithNotFoundStock(messageShortDto);
        } catch (JsonProcessingException ex) {
            log.error("can't parse message:{}", message, ex);
            sendErrorToKafkaWithParsing(messageShortDto);
            throw new KafkaException("can't parse message:" + message, ex);
        }
    }

    private void sendErrorToKafkaWithNotFoundStock(@Nonnull MessageShortDto messageShortDto) {
        messageShortDto.setMessage(String.format("Акция по тикеру '%S' не найдена.", messageShortDto.getMessage()));
        errorProducerService.sendError(messageShortDto);
    }

    private void sendErrorToKafkaWithParsing(@Nonnull MessageShortDto messageShortDto) {
        messageShortDto.setMessage(String.format("Ошибка при парсинге данных об акции '%S'.", messageShortDto.getMessage()));
        errorProducerService.sendError(messageShortDto);
    }
}
