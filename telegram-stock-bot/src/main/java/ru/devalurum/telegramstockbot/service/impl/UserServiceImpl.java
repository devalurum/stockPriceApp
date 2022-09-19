package ru.devalurum.telegramstockbot.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.devalurum.telegramstockbot.domain.dto.UserDto;
import ru.devalurum.telegramstockbot.domain.entity.User;
import ru.devalurum.telegramstockbot.domain.mapper.UserMapper;
import ru.devalurum.telegramstockbot.repository.UserRepository;
import ru.devalurum.telegramstockbot.service.UserService;
import ru.devalurum.telegramstockbot.service.PointReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PointReader pointReader;

    @Override
    @Transactional
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public User save(UserDto userDto) {
        return userRepository.save(userMapper.toModel(userDto));
    }

    @Override
    @Transactional
    public User save(Message message) {
        UserDto userDto = userMapper.createDto(message, LocalDateTime.now(ZoneOffset.UTC));

        Optional.ofNullable(message.getLocation())
                .ifPresent(userDto::setCoordinates);

        return userRepository.save(userMapper.toModel(userDto));
    }

    @Override
    @Transactional
    public User update(Message message) {
        long chatId = message.getChatId();

        return userRepository.findById(chatId)
                .map(user -> {
                    UserDto userDto = userMapper.fromMessageToDto(message);
                    user.setNickname(userDto.getNickname());
                    user.setFirstName(userDto.getFirstName());
                    user.setLastName(userDto.getLastName());
                    user.setCoordinates(pointReader.createPointFromTelegramLocation(userDto.getCoordinates()));

                    return userRepository.save(user);
                })
                .orElseGet(() -> save(message));
    }

    @Override
    @Transactional
    public User updateLocation(Message message) {
        long chatId = message.getChatId();

        return userRepository.findById(chatId)
                .map(user -> {
                    user.setCoordinates(pointReader.
                            createPointFromTelegramLocation(message.getLocation()));
                    return userRepository.save(user);
                })
                .orElseGet(() -> save(message));
    }


    @Override
    @Transactional
    public UserDto getById(long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Transactional
    public Optional<Boolean> existById(long id) {
        return userRepository.existsById(id);
    }

    @Override
    @Transactional
    public UserDto getByNickname(String email) {
        return userRepository.findUserByNickname(email)
                .map(userMapper::toDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Transactional
    public User update(@NonNull UserDto userDto) {
        return userRepository.findById(userDto.getChatId())
                .map(user -> {
                    userMapper.updateModel(userDto, user);
                    return userRepository.save(user);
                })
                .orElseThrow(EntityNotFoundException::new);
    }


    @Override
    @Transactional
    public void deleteById(long id) {
        userRepository.findById(id)
                .map(user -> {
                    userRepository.deleteById(user.getId());
                    return user;
                })
                .orElseThrow(EntityNotFoundException::new);
    }
}
