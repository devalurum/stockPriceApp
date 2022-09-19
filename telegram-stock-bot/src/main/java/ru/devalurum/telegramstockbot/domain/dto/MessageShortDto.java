package ru.devalurum.telegramstockbot.domain.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class MessageShortDto {
    long chatId;
    int messageId;
    String message;
}
