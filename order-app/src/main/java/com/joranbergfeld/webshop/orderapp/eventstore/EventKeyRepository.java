package com.joranbergfeld.webshop.orderapp.eventstore;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventKeyRepository extends JpaRepository<EventKeyEntity, String> {

    Optional<EventKeyEntity> findByEventKey(final String eventKey);
}
