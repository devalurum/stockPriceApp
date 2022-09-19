package ru.devalurum.telegramstockbot.repository;

import ru.devalurum.telegramstockbot.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByNickname(String nickname);

    Optional<Boolean> existsById(long id);

}