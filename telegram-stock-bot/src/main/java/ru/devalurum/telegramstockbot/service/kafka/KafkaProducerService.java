package ru.devalurum.telegramstockbot.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.devalurum.telegramstockbot.domain.dto.MessageShortDto;
import ru.devalurum.telegramstockbot.domain.mapper.MessageShortMapper;
import ru.devalurum.telegramstockbot.exception.KafkaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${kafka.general.topic.name}")
    private String topicName;

    @Value(value = "${kafka.stat_request.topic.name}")
    private String topicStatName;

    private final ObjectMapper mapper;
    private final MessageShortMapper messageShortMapper;

    public void sendMessage(Message message) {
        MessageShortDto messageWithTicker = messageShortMapper.createDto(message);

        String messageAsString;
        try {
            messageAsString = mapper.writeValueAsString(messageWithTicker);
        } catch (JsonProcessingException ex) {
            log.error("can't serialize message:{}", messageWithTicker, ex);
            throw new KafkaException("can't send message:" + messageWithTicker, ex);
        }

        var future
                = this.kafkaTemplate.send(topicName, messageAsString);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Sent ticker: '{}' with offset: {}", messageWithTicker, result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send ticker :{}", messageWithTicker, ex);
            }
        });
    }

    public void sendRequestStatMessage(Message message) {
        MessageShortDto messageWithTicker = messageShortMapper.createDto(message);

        String messageAsString;
        try {
            messageAsString = mapper.writeValueAsString(messageWithTicker);
            log.info("send request stat to kafka");
            kafkaTemplate.send(topicStatName, messageAsString);
        } catch (JsonProcessingException ex) {
            log.error("can't serialize message:{}", messageWithTicker, ex);
            throw new KafkaException("can't send message:" + messageWithTicker, ex);
        }
    }

}
