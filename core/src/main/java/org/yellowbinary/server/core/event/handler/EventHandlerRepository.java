package org.yellowbinary.server.core.event.handler;

public interface EventHandlerRepository {

    public EventHandler getEventHandler(String base, String with);

    public void storeEventHandler(String base, String with, String annotation, String handlerClass);

}
