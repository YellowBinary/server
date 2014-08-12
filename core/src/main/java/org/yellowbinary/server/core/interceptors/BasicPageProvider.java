package org.yellowbinary.server.core.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.yellowbinary.server.core.*;
import org.yellowbinary.server.core.dao.BasicPageDao;
import org.yellowbinary.server.core.dao.MetaDao;
import org.yellowbinary.server.core.model.content.BasicPage;
import org.yellowbinary.server.core.model.Meta;
import org.yellowbinary.server.core.model.RootNode;
import org.yellowbinary.server.core.service.NodeService;
import org.yellowbinary.server.core.stereotypes.Interceptor;
import org.yellowbinary.server.core.stereotypes.OnLoad;
import org.yellowbinary.server.core.stereotypes.Provides;

import java.util.Map;

/**
 * Provides and populates pages of type BasicPage.
 */
@Interceptor
public class BasicPageProvider {

    private static final Logger LOG = LoggerFactory.getLogger(BasicPageProvider.class);

    public static final String TYPE = BasicPage.TYPE;

    @Autowired
    private BasicPageDao basicPageDao;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private MetaDao metaDao;

    @Provides(base = Core.Base.NODE, with = TYPE)
    public BasicPage loadPage(RootNode node, String withType, Map<String, Object> args) throws NodeNotFoundException {

        BasicPage page = basicPageDao.findKeyAndVersion(node.getKey(), node.getVersion());
        if (page == null) {
            throw new NodeNotFoundException(node.getKey());
        }

        return page;
    }

    @OnLoad(base = Core.Base.NODE, with = TYPE)
    public void decoratePage(Node node, String withType, Map<String, Object> args) throws ModuleException, NodeLoadException, NodeNotFoundException {

        BasicPage page = (BasicPage) node;

        for (String blockIdentifier : page.getBlocks()) {
            Node block = nodeService.loadNode(blockIdentifier, 0);
            Meta meta = metaDao.findByKey(node.getKey(), 0, blockIdentifier);
            if (meta != null) {
                node.addChild(block, meta);
            }
            LOG.warn("No meta, using default");
            node.addChild(block, Meta.defaultMeta());
        }

        //TODO: Handle this somehow, in dev/admin maybe show a Element with a warning message and in prod swallow error?

    }

}
