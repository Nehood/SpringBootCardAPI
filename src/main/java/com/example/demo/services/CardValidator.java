package com.example.demo.services;

import com.example.demo.data.Card;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.sql.Date;

@Service
public class CardValidator {

    private final int CARD_NUMBER_LENGTH = 16;
    private final int CVC_CODE_LENGTH = 3;

    private Clock clock = Clock.systemUTC();

    public List<String> validateCard(final Card card) {
        List<String> validationIssues = new ArrayList<>();
        validationIssues.addAll(validateCardNumber(card.getNumber()));
        validationIssues.addAll(validateExpirationDate(card.getExpiration()));
        validationIssues.addAll(validateCvcCode(card.getCvc()));

        return validationIssues;
    }

    private List<String> validateCardNumber(final String cardNumber) {
        List<String> issues = new ArrayList<>();
        if (!cardNumber.matches("[0-9]+")) issues.add("Card number must consist of only numbers");
        if (cardNumber.length() != CARD_NUMBER_LENGTH)
            issues.add(String.format("Card number must be of length %s", CARD_NUMBER_LENGTH));
        return issues;
    }

    private List<String> validateExpirationDate(final Date expirationDate) {
        List<String> issues = new ArrayList<>();
        if (LocalDate.now()
                .isAfter(expirationDate.toLocalDate()))
            issues.add("Card expiration date has already passed");
        return issues;
    }

    private List<String> validateCvcCode(final String cvcCode) {
        List<String> issues = new ArrayList<>();
        if (!cvcCode.matches("[0-9]+")) issues.add("Cvc number must consist of only numbers");
        if (cvcCode.length() != CVC_CODE_LENGTH)
            issues.add(String.format("Cvc number must be of length %s", CVC_CODE_LENGTH));
        return issues;
    }
}
