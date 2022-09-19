package ru.devalurum.stockappstat;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.kafka.annotation.EnableKafka;


@SpringBootApplication
@EnableKafka
public class StockStatisticApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(StockStatisticApplication.class).run(args);
    }
}
