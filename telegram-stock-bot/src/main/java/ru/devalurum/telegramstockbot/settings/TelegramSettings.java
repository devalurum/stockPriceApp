package ru.devalurum.telegramstockbot.settings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConstructorBinding
@ConfigurationProperties("app.telegram")
@Validated
@Value
@RequiredArgsConstructor
@Getter
public class TelegramSettings {

    @NotNull
    String token;
    @NotNull
    String username;

}
