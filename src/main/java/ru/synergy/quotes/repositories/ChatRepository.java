package ru.synergy.quotes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.synergy.quotes.models.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}