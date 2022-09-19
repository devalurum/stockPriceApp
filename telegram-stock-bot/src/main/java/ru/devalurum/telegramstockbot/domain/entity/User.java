package ru.devalurum.telegramstockbot.domain.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "users_table")
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class User {

    @Id
    Long id;

    String nickname;

    String firstName;

    String lastName;

    @CreationTimestamp
    @Column(nullable = false)
    LocalDateTime registeredAt;

    Point coordinates;

}
