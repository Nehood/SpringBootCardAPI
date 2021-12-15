package com.example.demo.controllers;

import com.example.demo.data.Card;
import com.example.demo.repositories.CardRepository;
import com.example.demo.services.CardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;

@RestController
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardValidator cardValidator;

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
        // TODO: tests, cache, flyway
    }

    @GetMapping("/cards")
    public @ResponseBody Iterable<Card> getCards() {
        return cardRepository.findAll();
    }

    @GetMapping("/cards/{id}")
    public @ResponseBody Card getCard(@PathVariable final Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Card with id %d not found", id)));
    }
}
