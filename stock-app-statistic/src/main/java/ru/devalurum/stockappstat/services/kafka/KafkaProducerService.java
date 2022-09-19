package ru.devalurum.stockappstat.services.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.devalurum.stockappstat.domain.dto.MessageShortDto;
import ru.devalurum.stockappstat.domain.entity.StatEntity;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${kafka.stat_answer.topic.name}")
    private String topicName;
    private final ObjectMapper mapper;

    public void sendStat(MessageShortDto messageShortDto, StatEntity stat) {
        try {
            messageShortDto.setMessage(mapper.writeValueAsString(stat));
            String messageAsString = mapper.writeValueAsString(messageShortDto);
            log.info("send stat to kafka: " + stat);
            kafkaTemplate.send(topicName, messageAsString);
        } catch (JsonProcessingException ex) {
            log.error("can't serialize message:{}", messageShortDto, ex);
            throw new KafkaException("can't send message:" + messageShortDto, ex);
        }
    }
}
