package ru.devalurum.telegramstockbot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties
@EnableKafka
public class TelegramStockBotApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(TelegramStockBotApplication.class).run(args);
    }

}
