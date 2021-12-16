package com.example.demo.services;

import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import com.example.demo.data.Card;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CardValidatorTest {

    @Mock
    private Clock clock;

    @InjectMocks
    private CardValidator cardValidator;

    ObjectMapper objectMapper = new ObjectMapper();

    private Clock fixedClock;
    private final static LocalDate LOCAL_DATE = LocalDate.of(2000, 01, 02);

    @BeforeEach
    public void before(){
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        Mockito.when(clock.instant()).thenReturn(fixedClock.instant());
        Mockito.when(clock.getZone()).thenReturn(fixedClock.getZone());
    }

    @Test
    void shouldReturnNoValidationIssues() throws IOException {
        Card card = objectMapper.readValue(new File("src/test/resources/CardValidatorTest/ValidCard.json"), Card.class);

        List<String> validationIssues = cardValidator.validateCard(card);

        assertTrue(validationIssues.isEmpty());
    }

    @Test
    void shouldReturnValidationIssues() throws IOException {
        Card card = objectMapper.readValue(new File("src/test/resources/CardValidatorTest/InvalidNumberCard.json"), Card.class);

        List<String> validationIssues = cardValidator.validateCard(card);

        int expectedValidationIssuesCount = 2;
        assertEquals(expectedValidationIssuesCount, validationIssues.size());
    }
}