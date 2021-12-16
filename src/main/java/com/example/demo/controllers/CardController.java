package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

import com.example.demo.data.Card;
import com.example.demo.repositories.CardRepository;
import com.example.demo.services.CardValidator;
import com.example.demo.services.LRUCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardValidator cardValidator;

    @Autowired
    private LRUCache cache;

    @PostMapping("/card")
    public ResponseEntity<String> addCard(@Valid @RequestBody final Card card) {
        try {
            List<String> validationIssues = cardValidator.validateCard(card);
            if (validationIssues.isEmpty()) {
                return ResponseEntity.ok(String.format("Created card with id: %s", cardRepository.save(card).getId()));
            }
            return new ResponseEntity(String.join("\n", validationIssues), HttpStatus.BAD_REQUEST);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("Card with given number already exists"));
        }
    }

    @GetMapping("/cards")
    public @ResponseBody Iterable<Card> getCards() {
        return cardRepository.findAll();
    }

    @GetMapping("/cards/{id}")
    public @ResponseBody Card getCard(@PathVariable final Long id) {
        Optional<Card> cacheRead = cache.get(id);
        if (cacheRead.isEmpty()) {
            Optional<Card> cardRead = cardRepository.findById(id);
            if (cardRead.isPresent()) {
                cache.put(id, cardRead.get());
                return cardRead.get();
            } else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Card with id %d not found", id));
        } else return cacheRead.get();
//        return cardRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Card with id %d not found", id)));
    }
}
