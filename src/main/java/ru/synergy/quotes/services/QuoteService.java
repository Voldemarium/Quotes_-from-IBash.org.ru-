package ru.synergy.quotes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.synergy.quotes.models.Quote;
import ru.synergy.quotes.repositories.QuoteRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class QuoteService {
    @Autowired
    IbashOrg_Parser parser;

    @Autowired
    QuoteRepository repository;

    public List<Quote> getPage(int pageNumber) {
        List<Quote> quotes = new ArrayList<>();
        var map = parser.getPage(pageNumber);
        for (var entry : map.entrySet()) {
            var rawQuote = new Quote();
            rawQuote.setQuoteId(entry.getKey());
            rawQuote.setText(entry.getValue());
            var existed = repository.findByQuoteId(rawQuote.getQuoteId());
            if (existed.isEmpty()) {
                quotes.add(repository.save(rawQuote));
            } else {
                quotes.add(existed.get());
            }
        }
        return quotes;
    }

    public Quote getById(int id) {
        var existingQuote = repository.findByQuoteId(id);
        if (existingQuote.isPresent()) {
            return existingQuote.get();
        }
        var quoteEntry = parser.getById(id);
        if (quoteEntry == null) {
            return null;
        }
        var newQuote = new Quote();
        newQuote.setQuoteId(quoteEntry.getKey());
        newQuote.setText(quoteEntry.getValue());
        return repository.save(newQuote);
    }

    public Quote getRandom() {
        var quoteEntry = parser.getRandom();
        if (quoteEntry == null) {
            return null;
        }
        //  Проверяем, есть ли эта Quote в БД
        var existingQuote = repository.findByQuoteId(quoteEntry.getKey());
        if (existingQuote.isPresent()) {
            return existingQuote.get();
        }
        //если в БД нет, то сохраняем ее в БД
        var newQuote = new Quote();
        newQuote.setQuoteId(quoteEntry.getKey());
        newQuote.setText(quoteEntry.getValue());
        return repository.save(newQuote);
    }
}
