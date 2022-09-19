package ru.devalurum.tinkoffstockapp.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
public class MessageShortDto {
    private long chatId;
    private int messageId;
    private String message;
}
