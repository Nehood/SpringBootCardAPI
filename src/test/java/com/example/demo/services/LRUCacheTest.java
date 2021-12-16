package com.example.demo.services;

import java.util.Optional;

import com.example.demo.data.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class LRUCacheTest {

    private final long ID_ONE_LONG = 1L;
    private final long ID_TWO_LONG = 2L;
    private final long ID_THREE_LONG = 3L;
    private final int CACHE_SIZE = 2;

    LRUCache lruCache;

    @BeforeEach
    void setup() {
        lruCache = new LRUCache(CACHE_SIZE);
    }

    @Test
    void emptyCacheShouldReturnEmptyOptional() {
        Optional<Card> cacheRead = lruCache.get(ID_ONE_LONG);

        assertTrue(cacheRead.isEmpty());
    }

    @Test
    void cacheShouldReturnValue() {
        Card mockedCard = Mockito.mock(Card.class);

        lruCache.put(ID_ONE_LONG, mockedCard);
        Optional<Card> cacheRead = lruCache.get(ID_ONE_LONG);

        assertTrue(cacheRead.isPresent());
    }

    @Test
    void overflownCacheWillNotReturnOldestEntry() {
        Card mockedCard = Mockito.mock(Card.class);

        lruCache.put(ID_ONE_LONG, mockedCard);
        lruCache.put(ID_TWO_LONG, mockedCard);
        lruCache.put(ID_THREE_LONG, mockedCard);

        Optional<Card> cacheRead = lruCache.get(ID_ONE_LONG);

        assertTrue(cacheRead.isEmpty());
    }

    @Test
    void overflownCacheWillReturnCorrectKey() {
        Card mockedCard = Mockito.mock(Card.class);

        lruCache.put(ID_ONE_LONG, mockedCard);
        lruCache.put(ID_TWO_LONG, mockedCard);
        lruCache.put(ID_THREE_LONG, mockedCard);

        Optional<Card> cacheRead = lruCache.get(ID_TWO_LONG);

        assertTrue(cacheRead.isPresent());
    }

}