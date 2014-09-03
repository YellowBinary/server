package org.yellowbinary.server.backend.model.event;

import org.hibernate.annotations.Entity;
import org.yellowbinary.server.core.event.handler.EventHandler;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "handlers")
public class StoredEventHandler implements EventHandler {

    private static final String TYPE = "yellowbinary.eventhandler";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String annotation;
    private String base;
    private String with;
    private String handlerClass;

    public StoredEventHandler() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getWith() {
        return with;
    }

    public void setWith(String with) {
        this.with = with;
    }

    public String getHandlerClass() {
        return handlerClass;
    }

    public void setHandlerClass(String handlerClass) {
        this.handlerClass = handlerClass;
    }
}
