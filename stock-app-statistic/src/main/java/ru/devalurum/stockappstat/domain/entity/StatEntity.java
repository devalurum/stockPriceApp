package ru.devalurum.stockappstat.domain.entity;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatEntity {
    int id;
    LocalDateTime startTime;
    LocalDateTime updateTime;
    long countRequests;
}
