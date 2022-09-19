package ru.devalurum.stockappstat.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Slf4j
public class KafkaConfig {

    @Value(value = "${kafka.general.topic.name}")
    private String topicWithTickers;

    @Value(value = "${kafka.stat_request.topic.name}")
    private String topicWithStatRequest;

    @Value(value = "${kafka.stat_answer.topic.name}")
    private String topicWithAnswerStat;

    @Bean
    public NewTopic topicWithTickers() {
        return TopicBuilder
                .name(topicWithTickers)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicWithStatReq() {
        return TopicBuilder
                .name(topicWithStatRequest)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicWithStatAnswer() {
        return TopicBuilder
                .name(topicWithAnswerStat)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
