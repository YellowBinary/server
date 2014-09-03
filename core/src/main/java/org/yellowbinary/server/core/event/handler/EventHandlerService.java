package org.yellowbinary.server.core.event.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yellowbinary.server.core.annotation.CachedAnnotation;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.util.Collection;

@Service
public class EventHandlerService {

    private static final Logger LOG = LoggerFactory.getLogger(EventHandlerService.class);

    @Autowired(required = false)
    private EventHandlerRepository eventHandlerRepository;

    @PostConstruct
    public void init() {
        if (eventHandlerRepository == null) {
            eventHandlerRepository = new InMemoryEventHandlerRepository();
        }
    }

    public CachedAnnotation selectEventHandler(Class<? extends Annotation> annotationType, String base, String with, Collection<CachedAnnotation> cachedAnnotations) {
        if (cachedAnnotations.isEmpty()) {
            return null;
        }

        EventHandler storedEventHandlerType = eventHandlerRepository.getEventHandler(base, with);
        if (storedEventHandlerType == null) {
            return setFirstEventHandlerAsDefault(annotationType, base, with, cachedAnnotations.iterator().next());
        }

        for (CachedAnnotation cachedAnnotation : cachedAnnotations) {
            if (storedEventHandlerType.getHandlerClass().equals(cachedAnnotation.getMethod().getDeclaringClass().getName())) {
                return cachedAnnotation;
            }
        }
        LOG.error("The stored EventHandler [" + storedEventHandlerType + "] is not available, resetting the value for event [" + with + "]");
        return setFirstEventHandlerAsDefault(annotationType, base, with, cachedAnnotations.iterator().next());
    }

    private CachedAnnotation setFirstEventHandlerAsDefault(Class<? extends Annotation> annotationType, String base, String with, CachedAnnotation annotation) {
        LOG.info("Setting [" + annotation.getMethod().getDeclaringClass().getName() + "] as default for event [" + base + "] with [" + with + "]");
        eventHandlerRepository.storeEventHandler(base, with, annotationType.getName(), annotation.getMethod().getDeclaringClass().getName());
        return annotation;
    }
}
