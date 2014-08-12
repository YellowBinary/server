package org.yellowbinary.server.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yellowbinary.server.core.annotation.CachedAnnotation;
import org.yellowbinary.server.core.dao.EventHandlerDao;
import org.yellowbinary.server.core.model.EventHandler;

import java.lang.annotation.Annotation;
import java.util.Collection;

@Service
public class EventHandlerService {

    private static final Logger LOG = LoggerFactory.getLogger(EventHandlerService.class);

    @Autowired
    private EventHandlerDao eventHandlerDao;

    public CachedAnnotation selectEventHandler(Class<? extends Annotation> annotationType, String nodeType, String withType, Collection<CachedAnnotation> cachedAnnotations) {
        if (cachedAnnotations.isEmpty()) {
            return null;
        }

        EventHandler storedEventHandlerType = eventHandlerDao.findWithNodeTypeAndWithType(nodeType, withType);
        if (storedEventHandlerType == null) {
            return setFirstEventHandlerAsDefault(annotationType, nodeType, withType, cachedAnnotations);
        }

        for (CachedAnnotation cachedAnnotation : cachedAnnotations) {
            if (storedEventHandlerType.getHandlerClass().equals(cachedAnnotation.getMethod().getDeclaringClass().getName())) {
                return cachedAnnotation;
            }
        }
        LOG.error("The stored EventHandler [" + storedEventHandlerType + "] is not available, resetting the value for event [" + withType + "]");
        return setFirstEventHandlerAsDefault(annotationType, nodeType, withType, cachedAnnotations);
    }

    private CachedAnnotation setFirstEventHandlerAsDefault(Class<? extends Annotation> annotationType, String nodeType, String withType, Collection<CachedAnnotation> providers) {
        CachedAnnotation annotation = providers.iterator().next();
        LOG.info("Setting ["+annotation.getMethod().getDeclaringClass().getName()+"] as default for event ["+ nodeType +"] with ["+withType+"]");
        EventHandler eventHandler = new EventHandler();
        eventHandler.setNodeType(nodeType);
        eventHandler.setWithType(withType);
        eventHandler.setAnnotation(annotationType.getName());
        eventHandler.setHandlerClass(annotation.getMethod().getDeclaringClass().getName());
        eventHandlerDao.save(eventHandler);
        return annotation;
    }
}
