package com.joranbergfeld.webshop.paymentapp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequestMapping("/payment")
@RestController
public class PaymentController {

    private final PaymentRepository repository;

    public PaymentController(PaymentRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentEntity> getSingle(@PathVariable("id") final String id) {
        Optional<PaymentEntity> entity = repository.findById(id);
        return entity.map(paymentEntity -> new ResponseEntity<>(paymentEntity, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public List<PaymentEntity> getAll() {
        return repository.findAll();
    }
}
