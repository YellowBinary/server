package org.yellowbinary.server.core.model;

import javax.persistence.*;

@Entity
@Table(name = "handlers")
public class EventHandler {

    private static final String TYPE = "origo.eventhandler";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String annotation;
    private String nodeType;
    private String withType;
    private String handlerClass;

    public EventHandler() {
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

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getWithType() {
        return withType;
    }

    public void setWithType(String withType) {
        this.withType = withType;
    }

    public String getHandlerClass() {
        return handlerClass;
    }

    public void setHandlerClass(String handlerClass) {
        this.handlerClass = handlerClass;
    }
}
