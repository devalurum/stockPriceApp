package ru.devalurum.telegramstockbot.domain.mapper;

import ru.devalurum.telegramstockbot.domain.dto.MessageShortDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageShortMapper {

    @Mapping(target = "chatId", source = "message.chatId")
    @Mapping(target = "messageId", source = "message.messageId")
    @Mapping(target = "message", source = "message.text")
    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    MessageShortDto createDto(Message message);
}
