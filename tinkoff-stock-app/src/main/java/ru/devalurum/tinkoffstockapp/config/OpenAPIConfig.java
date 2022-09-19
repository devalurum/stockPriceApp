package ru.devalurum.tinkoffstockapp.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenAPIConfig {

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi
                .builder()
                .group("Api")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tinkoff StockPrice API")
                        .description("API для получения цен акций.")
                        .contact(new Contact().name("Alexander Urumov").url("https://t.me/devalurum"))
                        .version("v1.0.0")
                        .license(new License().name("Apache License Version 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("Github source")
                        .url("https://github.com/devalurum/messenger-system"))
                .externalDocs(new ExternalDocumentation()
                        .description("на основе Tinkoff Invest API")
                        .url("https://github.com/Tinkoff/invest-api-java-sdk"));
    }
}