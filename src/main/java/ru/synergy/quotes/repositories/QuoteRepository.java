package ru.synergy.quotes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.synergy.quotes.models.Quote;

import java.util.Optional;

public interface QuoteRepository extends JpaRepository<Quote, Integer> {
    Optional<Quote> findByQuoteId(Integer quoteId);
}