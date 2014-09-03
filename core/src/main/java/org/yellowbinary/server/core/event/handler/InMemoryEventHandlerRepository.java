package org.yellowbinary.server.core.event.handler;

import java.util.Map;

public class InMemoryEventHandlerRepository implements EventHandlerRepository {

    public Map<String, EventHandler> handlers;

    @Override
    public EventHandler getEventHandler(String base, String with) {
        return handlers.get(base + with);
    }

    @Override
    public void storeEventHandler(final String base, final String with, final String annotation, final String handlerClass) {
        handlers.put(base + with, new EventHandler() {

            @Override
            public String getAnnotation() {
                return annotation;
            }

            @Override
            public String getBase() {
                return base;
            }

            @Override
            public String getWith() {
                return with;
            }

            @Override
            public String getHandlerClass() {
                return handlerClass;
            }
        });
    }

}
