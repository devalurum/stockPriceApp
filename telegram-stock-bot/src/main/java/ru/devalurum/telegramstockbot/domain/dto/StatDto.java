package ru.devalurum.telegramstockbot.domain.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class StatDto {
    LocalDateTime startTime;
    LocalDateTime updateTime;
    long countRequests;
}
