package com.joranbergfeld.webshop.stockapp.item;

import com.joranbergfeld.webshop.stockapp.StockEntity;
import com.joranbergfeld.webshop.stockapp.StockRepository;
import com.joranbergfeld.webshop.stockapp.item.event.ItemCreatedEvent;
import com.joranbergfeld.webshop.stockapp.item.event.ItemDeletedEvent;
import com.joranbergfeld.webshop.stockapp.item.event.ItemUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Optional;

public class ItemManager {

    private final Logger log = LoggerFactory.getLogger(ItemManager.class);
    private final StockRepository stockRepository;

    public ItemManager(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @KafkaListener(topics = "${stock-app.events.item-created-topic}")
    public void createStockForItem(ItemCreatedEvent event) {
        StockEntity entity = new StockEntity();
        entity.setItemId(event.itemId());
        entity.setAmount(event.initialAmount());
        stockRepository.save(entity);
    }

    @KafkaListener(topics = "${stock-app.events.item-updated-topic}")
    public void updateStockForItem(ItemUpdatedEvent event) {
        final String itemId = event.itemId();
        Optional<StockEntity> byId = stockRepository.findById(itemId);
        if (byId.isEmpty()) {
            log.warn("Got event that item with id {} is updated. However, we didn't find it.", itemId);
            return;
        }
        StockEntity record = byId.get();
        record.setAmount(event.updatedAmount());
        stockRepository.save(record);
    }

    @KafkaListener(topics = "${stock-app.events.item-deleted-topic}")
    public void deleteStockForItem(ItemDeletedEvent event) {
        final String itemId = event.itemId();
        Optional<StockEntity> byId = stockRepository.findById(itemId);
        if (byId.isEmpty()) {
            log.warn("Got event that item with id {} is deleted. However, we didn't find it.", itemId);
            return;
        }
        stockRepository.deleteById(byId.get().getStockId());
    }
}
