package ru.synergy.quotes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.synergy.quotes.models.Quote;
import ru.synergy.quotes.repositories.QuoteRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class QuoteService {
    @Autowired
    IbashOrg_Parser parser;

    @Autowired
    QuoteRepository repository;

    public List<Quote> getIndex() {
        List<Quote> quotes = new ArrayList<>();
        try {
            var map = parser.getIndex();
            for (var entry : map.entrySet()) {
                var rawQuote = new Quote();
                rawQuote.setQuoteId(entry.getKey());
                rawQuote.setText(entry.getValue());
                var existed = repository.findByQuoteId(rawQuote.getQuoteId());
                if (existed.isEmpty()) {
                   quotes.add(repository.save(rawQuote));
                } else {
                    quotes.add(rawQuote);
                }
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
        return quotes;
    }
}
