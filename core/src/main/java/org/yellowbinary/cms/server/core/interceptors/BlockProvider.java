package org.yellowbinary.cms.server.core.interceptors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.yellowbinary.cms.server.core.*;
import org.yellowbinary.cms.server.core.dao.BlockDao;
import org.yellowbinary.cms.server.core.model.Block;
import org.yellowbinary.cms.server.core.model.RootNode;
import org.yellowbinary.cms.server.core.service.NodeService;
import org.yellowbinary.cms.server.core.stereotypes.Interceptor;
import org.yellowbinary.cms.server.core.stereotypes.OnLoad;
import org.yellowbinary.cms.server.core.stereotypes.Provides;

import java.util.Map;

@Interceptor
public class BlockProvider {

    @Autowired
    private NodeService nodeService;

    @Autowired
    private BlockDao blockDao;

    @Provides(base = Core.Base.NODE, with = Block.TYPE)
    public Node createBlock(RootNode node, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException, NodeNotFoundException {

        Block block = blockDao.findByKeyAndVersion(node.getKey());
        if (block == null) {
            throw new NodeNotFoundException(node.getKey());
        }
        return block;
    }

    @OnLoad(base = Core.Base.NODE, with = Block.TYPE)
    public Element decorateBlock(Node node, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException, NodeNotFoundException {
        Block block = (Block) node;
        if (block != null && !StringUtils.isBlank(block.getReferenceId())) {
            Element element = (Element)nodeService.load(block.getReferenceId());
            if (element != null) {
                return element;
            }
        }

        //TODO: Handle this somehow, in dev/admin maybe show a Element with a warning message and in prod swallow error?
        return null;
    }

}
