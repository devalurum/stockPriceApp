package ru.devalurum.telegramstockbot.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value(value = "${kafka.general.topic.name}")
    private String topicWithTickers;

    @Value(value = "${kafka.stock.topic.name}")
    private String topicWithStocks;

    @Value(value = "${kafka.log.topic.name}")
    private String topicWithLog;

    @Value(value = "${kafka.stat_request.topic.name}")
    private String topicWithStat;

    @Value(value = "${kafka.stat_answer.topic.name}")
    private String topicWithStatAns;

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
                .name(topicWithStat)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicWithStatAns() {
        return TopicBuilder
                .name(topicWithStatAns)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder
                .name(topicWithStocks)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic log() {
        return TopicBuilder
                .name(topicWithLog)
                .partitions(1)
                .replicas(1)
                .build();
    }

}