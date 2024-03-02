package ru.synergy.quotes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.synergy.quotes.models.Quote;
import ru.synergy.quotes.repositories.QuoteRepository;
import ru.synergy.quotes.services.QuoteService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private QuoteService service;

    @Autowired
    private QuoteRepository repository;

    @GetMapping("/all") // simple: "http://localhost:8080/api/all?page=4"
    public ResponseEntity<List<Quote>> getAll(@RequestParam(required = false, defaultValue = "1") String page) {
        int _page = 1;
        try {
            _page = Integer.parseInt(page);
        } catch (Exception ignored) {}
                                     //PageRequest.of - деление результата по страницам (по 5 элементов на странице)
        var res_0 = repository.findAll();
        var res = repository.findAll(PageRequest.of(_page - 1, 5));
        return new ResponseEntity<>(res.stream().collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/page")    // simple: "http://localhost:8080/api/page?pageNumber=2"
    public ResponseEntity<List<Quote>> getPage(@RequestParam(required = false, defaultValue = "1") String pageNumber) {
        int _pageNumber = 1;
        try {
            _pageNumber = Integer.parseInt(pageNumber);
        } catch (Exception ignored) {}
        var res = service.getPage(_pageNumber);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{id}")     // id = quoteId,   simple: "http://localhost:8080/api/18737"
    public ResponseEntity<Quote> getById(@PathVariable("id") int id) {
        var res = service.getById(id);
        if (res == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/random")     // simple: http://localhost:8080/api/random
    public ResponseEntity<Quote> getRandom() {
        var res = service.getRandom();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}