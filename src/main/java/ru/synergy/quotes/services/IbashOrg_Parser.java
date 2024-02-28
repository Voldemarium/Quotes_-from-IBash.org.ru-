package ru.synergy.quotes.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class IbashOrg_Parser {
    public Map<Integer, String> getIndex() throws IOException {
        Document doc = Jsoup.connect("http://ibash.org.ru/").get();
        System.out.println(doc.title());
        Elements sourceQuotes = doc.select(".quote");
        Map<Integer, String> quotes = new HashMap<>();
        for (Element quoteElement : sourceQuotes) {
            int id = Integer.parseInt(Objects.requireNonNull(quoteElement
                    .select("b").first()).text().substring(1));
            String text = Objects.requireNonNull(quoteElement.select(".quotbody").first()).text();
            quotes.put(id, text);
        }
        return quotes;
    }
}
