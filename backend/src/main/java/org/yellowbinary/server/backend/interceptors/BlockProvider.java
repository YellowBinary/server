package org.yellowbinary.server.backend.interceptors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.yellowbinary.server.backend.Backend;
import org.yellowbinary.server.backend.Node;
import org.yellowbinary.server.backend.NodeLoadException;
import org.yellowbinary.server.backend.NodeNotFoundException;
import org.yellowbinary.server.core.*;
import org.yellowbinary.server.backend.dao.BlockDao;
import org.yellowbinary.server.backend.dao.MetaDao;
import org.yellowbinary.server.backend.model.content.Block;
import org.yellowbinary.server.backend.model.Meta;
import org.yellowbinary.server.backend.model.RootNode;
import org.yellowbinary.server.backend.service.NodeService;
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

    @Provides(base = Backend.Base.NODE, with = Block.TYPE)
    public Node createBlock(RootNode node, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException, NodeNotFoundException {

        Block block = blockDao.findByKeyAndVersion(node.getKey());
        if (block == null) {
            throw new NodeNotFoundException(node.getKey());
        }
        return block;
    }

    @OnLoad(base = Backend.Base.NODE, with = Block.TYPE)
    public void decorateBlock(Node node, Map<String, Object> args) throws NodeLoadException, ModuleException, NodeNotFoundException {
        Block block = (Block) node;
        if (block != null && !StringUtils.isBlank(block.getReferenceId())) {
            try {
                Node referencedNode = nodeService.load(block.getReferenceId());
                if (referencedNode != null) {
                    Meta meta = metaDao.findByKey(referencedNode.getKey(), 0, block.getReferenceId());
                    if (meta != null) {
                        referencedNode.addChild(referencedNode, meta);
                    }
                    LOG.info("No meta, using default");
                    node.addChild(referencedNode, Meta.defaultMeta());
                }
            } catch (AccessDeniedException e) {
                LOG.debug(String.format("Access denied for %s", block.getReferenceId()));
            }
        }

        //TODO: Handle this somehow, in dev/admin maybe show a Element with a warning message and in prod swallow error?
    }

}
