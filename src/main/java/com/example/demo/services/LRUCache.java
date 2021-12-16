package com.example.demo.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.example.demo.data.Card;
import com.example.demo.data.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LRUCache {
    private final Map<Long, Node> map;
    private final int capacity;

    private Node head = null;
    private Node tail = null;

    public LRUCache(@Value("${cache.capacity}") int capacity) {
        this.map = new HashMap<>();
        this.capacity = capacity;
    }

    public Optional<Card> get(Long key) {
        if (!map.containsKey(key)) {
            log.info("cache miss");
            return Optional.empty();
        }

        Node node = map.get(key);

        deleteFromList(node);
        setListHead(node);

        return Optional.of(node.getValue());
    }

    public void put(Long key, Card value) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.setValue(value);

            deleteFromList(node);
            setListHead(node);
        } else {
            if (map.size() >= capacity) {
                log.info("cache overflow");
                map.remove(tail.getKey());
                deleteFromList(tail);
            }

            Node node = new Node(key, value);

            map.put(key, node);
            setListHead(node);
        }
    }

    private void setListHead(Node node) {
        node.setNext(head);
        node.setPrev(null);

        if (null != head) {
            head.setPrev(node);
        } else {
            tail = node;
        }

        head = node;
    }

    private void deleteFromList(Node node) {
        if (head == node) {
            head = node.getNext();
        }

        if (null != node.getNext()) {
            node.getNext().setPrev(node.getPrev());
        }

        if (null != node.getPrev()) {
            node.getPrev().setNext(node.getNext());
        }
    }
}
