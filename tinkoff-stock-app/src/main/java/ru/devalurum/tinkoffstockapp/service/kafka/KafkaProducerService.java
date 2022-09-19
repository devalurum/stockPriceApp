package ru.devalurum.tinkoffstockapp.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.devalurum.tinkoffstockapp.domain.dto.MessageShortDto;
import ru.devalurum.tinkoffstockapp.domain.dto.StockDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> userKafkaTemplate;
    private final ObjectMapper mapper;


    @Value(value = "${kafka.stock.topic.name}")
    private String stockTopicName;

    public void sendStock(MessageShortDto messageDto) {

        String stockAsString;

        try {
            stockAsString = mapper.writeValueAsString(messageDto);
        } catch (JsonProcessingException ex) {
            log.error("can't serialize message:{}", messageDto, ex);
            throw new KafkaException("can't send message:" + messageDto, ex);
        }


        var future
                = this.userKafkaTemplate.send(stockTopicName, stockAsString);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Stock send: '{}' with offset: {}", messageDto, result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Stock created :{}", messageDto, ex);
            }
        });
    }
}
