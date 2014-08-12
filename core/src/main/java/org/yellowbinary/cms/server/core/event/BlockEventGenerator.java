package org.yellowbinary.cms.server.core.event;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yellowbinary.cms.server.core.*;
import org.yellowbinary.cms.server.core.model.content.Block;

import java.util.Collections;

@Component
public class BlockEventGenerator {

    @Autowired
    private ProvidesEventGenerator providesEventGenerator;

    @Autowired
    private OnLoadEventGenerator onLoadEventGenerator;

    public Node triggerBlockProvider(Node node, String withType, Block block) throws ModuleException, NodeLoadException {
        return providesEventGenerator.triggerInterceptor(node, Core.Base.NODE, withType, block, Maps.<String, Object>newHashMap());
    }

    public void triggerBeforeBlockLoaded(Node node, String withType, String identifier) throws ModuleException, NodeLoadException {
        onLoadEventGenerator.triggerBeforeInterceptor(node, Core.Base.NODE, withType, Collections.<String, Object>singletonMap("identifier", identifier));
    }

    public void triggerAfterBlockLoaded(Node node, String withType, Block block) throws ModuleException, NodeLoadException {
        onLoadEventGenerator.triggerAfterInterceptor(node, Core.Base.NODE, withType, Collections.<String, Object>singletonMap("block", block));
    }

}
