package org.yellowbinary.cms.server.core.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.yellowbinary.cms.server.core.*;
import org.yellowbinary.cms.server.core.dao.BasicPageDao;
import org.yellowbinary.cms.server.core.dao.MetaDao;
import org.yellowbinary.cms.server.core.model.BasicPage;
import org.yellowbinary.cms.server.core.model.Meta;
import org.yellowbinary.cms.server.core.model.RootNode;
import org.yellowbinary.cms.server.core.service.NodeService;
import org.yellowbinary.cms.server.core.stereotypes.Interceptor;
import org.yellowbinary.cms.server.core.stereotypes.OnLoad;
import org.yellowbinary.cms.server.core.stereotypes.Provides;

import java.util.Map;

/**
 * Provides and populates pages of type BasicPage.
 */
@Interceptor
public class BasicPageProvider {

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
    public void loadContent(Node node, String withType, Map<String, Object> args) throws ModuleException, NodeLoadException, NodeNotFoundException {

        BasicPage page = (BasicPage) node;

        for (String blockIdentifier : page.getBlocks()) {
            Element element = (Element) nodeService.loadNode(blockIdentifier, 0);
            Meta meta = metaDao.findByKey(node.getKey(), node.getVersion(), blockIdentifier);
            node.addElement(element, meta);
        }
    }

}
