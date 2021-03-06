package org.yellowbinary.server.backend.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.yellowbinary.server.backend.Backend;
import org.yellowbinary.server.backend.Node;
import org.yellowbinary.server.backend.NodeLoadException;
import org.yellowbinary.server.backend.NodeNotFoundException;
import org.yellowbinary.server.core.*;
import org.yellowbinary.server.backend.dao.BasicPageDao;
import org.yellowbinary.server.backend.dao.MetaDao;
import org.yellowbinary.server.backend.model.content.BasicPage;
import org.yellowbinary.server.backend.model.Meta;
import org.yellowbinary.server.backend.model.RootNode;
import org.yellowbinary.server.backend.service.NodeService;
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

    @Provides(base = Backend.Base.NODE, with = TYPE)
    public BasicPage loadPage(RootNode node, String withType, Map<String, Object> args) throws NodeNotFoundException {

        BasicPage page = basicPageDao.findKeyAndVersion(node.getKey(), node.getVersion());
        if (page == null) {
            throw new NodeNotFoundException(node.getKey());
        }

        return page;
    }

    @OnLoad(base = Backend.Base.NODE, with = TYPE)
    public void decoratePage(Node node, Map<String, Object> args) throws ModuleException, NodeLoadException, NodeNotFoundException {

        BasicPage page = (BasicPage) node;

        for (String blockIdentifier : page.getBlocks()) {
            try {
                Node block = nodeService.load(blockIdentifier, 0);
                Meta meta = metaDao.findByKey(node.getKey(), 0, blockIdentifier);
                if (meta != null) {
                    node.addChild(block, meta);
                }
                LOG.warn("No meta, using default");
                node.addChild(block, Meta.defaultMeta());
            } catch (AccessDeniedException e) {
                LOG.debug(String.format("Access denied for %s", blockIdentifier));
            }
        }

        //TODO: Handle this somehow, in dev/admin maybe show a Element with a warning message and in prod swallow error?

    }

}
