package com.joranbergfeld.webshop.orderapp.eventstore;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "eventkey")
public class EventKeyEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String eventKey;

    public EventKeyEntity() {
    }

    public EventKeyEntity(String eventKey) {
        this(null, eventKey);
    }

    public EventKeyEntity(String id, String eventKey) {
        this.id = id;
        this.eventKey = eventKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }
}
