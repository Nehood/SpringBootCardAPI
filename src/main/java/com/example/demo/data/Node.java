package com.example.demo.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Node {
    private final Long key;
    private Card value;

    private Node prev;
    private Node next;

    public Node(Long key, Card value){
        this.key = key;
        this.value = value;
    }
}
