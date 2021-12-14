package com.example.demo.controllers;

import com.example.demo.data.Card;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CardController {

    @PostMapping("/addCard")
    public String addCard(@Valid @RequestBody Card card) {
        return String.format("Returning card with cvc: %s", card.getCvc());

        // TODO: exception handling (i.e wrong date provided), Validator service, save to database, read from database, cache
    }
}
