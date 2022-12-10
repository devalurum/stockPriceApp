package ru.devalurum.tinkoffstockapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.piapi.core.InvestApi;

@Configuration
public class TinkoffApiConfig {

    @Value("${app.tinkoff.token}")
    private String tinkoffToken;

    @Bean
    public InvestApi tinkoffConfigApi() {
        System.out.println(tinkoffToken);
        return InvestApi.createReadonly(tinkoffToken);
    }
}
