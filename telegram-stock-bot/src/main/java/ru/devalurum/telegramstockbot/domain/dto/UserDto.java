package ru.devalurum.telegramstockbot.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
public class UserDto implements Serializable {
    Long chatId;
    String nickname;
    String firstName;
    String lastName;
    LocalDateTime registeredAt;
    Location coordinates;
}
