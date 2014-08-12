package org.yellowbinary.server.core.interceptors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.yellowbinary.server.core.*;
import org.yellowbinary.server.core.dao.BlockDao;
import org.yellowbinary.server.core.dao.MetaDao;
import org.yellowbinary.server.core.model.content.Block;
import org.yellowbinary.server.core.model.Meta;
import org.yellowbinary.server.core.model.RootNode;
import org.yellowbinary.server.core.service.NodeService;
import org.yellowbinary.server.core.stereotypes.Interceptor;
import org.yellowbinary.server.core.stereotypes.OnLoad;
import org.yellowbinary.server.core.stereotypes.Provides;

import java.util.Map;

@Interceptor
public class BlockProvider {

    private static final Logger LOG = LoggerFactory.getLogger(BlockProvider.class);

    @Autowired
    private NodeService nodeService;

    @Autowired
    private BlockDao blockDao;

    @Autowired
    private MetaDao metaDao;

    @Provides(base = Core.Base.NODE, with = Block.TYPE)
    public Node createBlock(RootNode node, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException, NodeNotFoundException {

        Block block = blockDao.findByKeyAndVersion(node.getKey());
        if (block == null) {
            throw new NodeNotFoundException(node.getKey());
        }
        return block;
    }

    @OnLoad(base = Core.Base.NODE, with = Block.TYPE)
    public void decorateBlock(Node node, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException, NodeNotFoundException {
        Block block = (Block) node;
        if (block != null && !StringUtils.isBlank(block.getReferenceId())) {
            Node text = nodeService.load(block.getReferenceId());
            if (text != null) {
                Meta meta = metaDao.findByKey(node.getKey(), 0, block.getReferenceId());
                if (meta != null) {
                    node.addChild(text, meta);
                }
                LOG.warn("No meta, using default");
                node.addChild(text, Meta.defaultMeta());
            }
        }

        //TODO: Handle this somehow, in dev/admin maybe show a Element with a warning message and in prod swallow error?
    }

}
