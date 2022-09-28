package com.joranbergfeld.webshop.stockapp;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<StockEntity, String> {

    Optional<StockEntity> findByItemId(String itemId);
}
