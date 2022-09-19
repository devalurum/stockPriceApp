package ru.devalurum.stockappstat.services.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.devalurum.stockappstat.domain.dto.MessageShortDto;
import ru.devalurum.stockappstat.domain.entity.StatEntity;
import ru.devalurum.stockappstat.exception.KafkaException;
import ru.devalurum.stockappstat.services.StatService;
import ru.devalurum.stockappstat.services.TelegramRequestStatisticsProcessor;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final TelegramRequestStatisticsProcessor telegramRequestStatisticsProcessor;
    private final ObjectMapper mapper;
    private final StatService statService;
    private final KafkaProducerService kafkaProducerService;

    @KafkaListener(topics = "${kafka.general.topic.name}",
            groupId = "${kafka.general.topic.group.id}")
    public void rateRequestListen(String msgAsString) {
        MessageShortDto message;
        try {
            message = mapper.readValue(msgAsString, MessageShortDto.class);
        } catch (Exception ex) {
            log.error("can't parse message:{}", msgAsString, ex);
            throw new KafkaException("can't parse message:" + msgAsString, ex);
        }
        telegramRequestStatisticsProcessor.processMessage(message);
    }

    @KafkaListener(topics = "${kafka.stat_request.topic.name}",
            groupId = "${kafka.stat_request.topic.group.id}")
    public void consumeStat(String requestStat) {
        MessageShortDto message;
        try {
            message = mapper.readValue(requestStat, MessageShortDto.class);
            log.info("request stat info: " + message);

            StatEntity stat = statService.getStat()
                    .orElse(
                            StatEntity.builder()
                                    .countRequests(0)
                                    .build()
                    );

            kafkaProducerService.sendStat(message, stat);
        } catch (Exception ex) {
            log.error("can't parse message:{}", requestStat, ex);
            throw new KafkaException("can't parse message:" + requestStat, ex);
        }
    }
}
