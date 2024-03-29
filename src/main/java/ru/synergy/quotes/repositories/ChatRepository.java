package ru.synergy.quotes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.synergy.quotes.models.Chat;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByChatId(Long chatId);
}