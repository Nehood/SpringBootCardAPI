package com.example.demo.repositories;

import com.example.demo.data.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository
        extends JpaRepository<Card, Long> {
}
