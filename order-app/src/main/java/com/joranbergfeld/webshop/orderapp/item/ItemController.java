package com.joranbergfeld.webshop.orderapp.item;

import com.joranbergfeld.webshop.orderapp.TopicConfiguration;
import com.joranbergfeld.webshop.orderapp.item.event.ItemCreatedEvent;
import com.joranbergfeld.webshop.orderapp.item.event.ItemUpdatedEvent;
import com.joranbergfeld.webshop.orderapp.item.request.CreateItemRequest;
import com.joranbergfeld.webshop.orderapp.item.request.UpdateItemRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemRepository repository;
    private final TopicConfiguration topicConfiguration;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ItemController(ItemRepository repository, TopicConfiguration topicConfiguration, KafkaTemplate<String, Object> kafkaTemplate) {
        this.repository = repository;
        this.topicConfiguration = topicConfiguration;
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping
    public List<ItemEntity> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<ItemEntity> createEntity(@RequestBody CreateItemRequest request) {
        ItemEntity item = new ItemEntity(request.name(), request.url());
        ItemEntity savedItem = repository.save(item);
        kafkaTemplate.send(topicConfiguration.itemCreatedTopic(), new ItemCreatedEvent(savedItem.getId(), request.amount()));
        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemEntity> updateEntity(@PathVariable("id") String id, @RequestBody UpdateItemRequest request) {
        Optional<ItemEntity> byId = repository.findById(id);
        if (byId.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ItemEntity item = byId.get();
        item.setName(request.name());
        item.setUrl(request.url());
        ItemEntity savedItem = repository.save(item);
        kafkaTemplate.send(topicConfiguration.itemUpdatedTopic(), new ItemUpdatedEvent(item.getId(), request.amount()));
        return new ResponseEntity<>(savedItem, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ItemEntity> deleteEntity(@PathVariable("id") String id) {
        Optional<ItemEntity> byId = repository.findById(id);
        if (byId.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        repository.deleteById(id);
        ItemEntity item = byId.get();
        return new ResponseEntity<>(item, HttpStatus.OK);
    }
}
