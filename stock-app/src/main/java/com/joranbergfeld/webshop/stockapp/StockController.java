package com.joranbergfeld.webshop.stockapp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequestMapping("/stock")
@RestController
public class StockController {

    private final StockRepository repository;

    public StockController(StockRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockEntity> getSingle(@PathVariable("id") final String id) {
        Optional<StockEntity> entity = repository.findById(id);
        return entity.map(stockEntity -> new ResponseEntity<>(stockEntity, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<StockEntity> getSingleByItemId(@PathVariable("id") final String itemId) {
        Optional<StockEntity> stock = repository.findByItemId(itemId);
        return stock.map(s -> new ResponseEntity<>(s, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public List<StockEntity> getAll() {
        return repository.findAll();
    }

}
