package com.joranbergfeld.webshop.orderapp.order;

import com.joranbergfeld.webshop.orderapp.TopicConfiguration;
import com.joranbergfeld.webshop.orderapp.item.ItemRepository;
import com.joranbergfeld.webshop.orderapp.order.event.OrderSubmittedEvent;
import com.joranbergfeld.webshop.orderapp.order.request.CreateOrderRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final TopicConfiguration topicConfiguration;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    
    public OrderController(TopicConfiguration topicConfiguration, KafkaTemplate<String, Object> kafkaTemplate, ItemRepository itemRepository, OrderRepository orderRepository) {
        this.topicConfiguration = topicConfiguration;
        this.kafkaTemplate = kafkaTemplate;
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public ResponseEntity<OrderEntity> createEntity(@RequestBody CreateOrderRequest request) {
        checkIfItemIdExists(request);
        OrderEntity savedItem = saveOrder(request);
        kafkaTemplate.send(topicConfiguration.orderSubmittedTopic(), new OrderSubmittedEvent(savedItem.getOrderId(), request.itemId(), request.amount()));
        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }

    @GetMapping
    public List<OrderEntity> getAll() {
        return orderRepository.findAll();
    }

    private OrderEntity saveOrder(CreateOrderRequest request) {
        OrderEntity item = new OrderEntity(request.by(), request.itemId(), request.amount(), OrderState.PENDING, PaymentState.PENDING, StockState.PENDING);
        return orderRepository.save(item);
    }

    private void checkIfItemIdExists(CreateOrderRequest request) {
        itemRepository.findById(request.itemId()).orElseThrow(() -> new RuntimeException("ItemID not found."));
    }
}
