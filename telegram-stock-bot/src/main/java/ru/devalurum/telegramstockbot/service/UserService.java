package ru.devalurum.telegramstockbot.service;

import ru.devalurum.telegramstockbot.domain.dto.UserDto;
import ru.devalurum.telegramstockbot.domain.entity.User;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> findAll();

    User save(UserDto userDto);

    User save(Message message);

    User update(Message message);

    User update(UserDto userDto);

    User updateLocation(Message message);

    UserDto getById(long id);

    Optional<Boolean> existById(long id);

    UserDto getByNickname(String nickname);

    void deleteById(long id);
}
