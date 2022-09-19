package ru.devalurum.telegramstockbot.domain.mapper;


import ru.devalurum.telegramstockbot.domain.dto.UserDto;
import ru.devalurum.telegramstockbot.domain.entity.User;
import ru.devalurum.telegramstockbot.service.PointReader;
import org.locationtech.jts.geom.Point;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = PointReader.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class UserMapper {

    @Autowired
    private PointReader pointReader;

    @InheritInverseConfiguration
    @Mapping(target = "id", source = "chatId")
    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "coordinates", source = "coordinates", qualifiedByName = "locationToPoint")
    public abstract User toModel(UserDto userDto);

    @Mapping(source = "coordinates", target = "coordinates", qualifiedByName = "pointToLocation")
    public abstract UserDto toDto(User user);

    @Mapping(source = "coordinates", target = "coordinates", qualifiedByName = "locationToPoint")
    public abstract void updateModel(UserDto userDto,
                                     @MappingTarget User user);

    @Mapping(target = "nickname", source = "chat.userName")
    @Mapping(target = "chatId", source = "chat.id")
    @Mapping(target = "registeredAt", source = "registeredAt")
    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    public abstract UserDto createDto(Chat chat, LocalDateTime registeredAt);

    @Mapping(target = "nickname", source = "message.chat.userName")
    @Mapping(target = "chatId", source = "message.chatId")
    @Mapping(target = "firstName", source = "message.chat.firstName")
    @Mapping(target = "lastName", source = "message.chat.lastName")
    @Mapping(target = "coordinates", source = "message.location")
    @Mapping(target = "registeredAt", source = "registeredAt")
    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    public abstract UserDto createDto(Message message, LocalDateTime registeredAt);

    @Mapping(target = "nickname", source = "chat.userName")
    @Mapping(target = "chatId", source = "chat.id")
    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    public abstract UserDto fromChatToDto(Chat chat);

    @Mapping(target = "nickname", source = "message.chat.userName")
    @Mapping(target = "chatId", source = "message.chatId")
    @Mapping(target = "firstName", source = "message.chat.firstName")
    @Mapping(target = "lastName", source = "message.chat.lastName")
    @Mapping(target = "coordinates", source = "message.location")
    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    public abstract UserDto fromMessageToDto(Message message);

    @Named("pointToLocation")
    public Location pointToLocation(Point point) {
        if (point == null)
            return null;
        return pointReader.convertPointToTelegramLocation(point);
    }

    @Named("locationToPoint")
    public Point locationToPoint(Location location) {
        if (location == null)
            return null;
        return pointReader.createPointFromTelegramLocation(location);
    }
}