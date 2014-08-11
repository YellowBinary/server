package org.yellowbinary.cms.server.core.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yellowbinary.cms.server.core.*;
import org.yellowbinary.cms.server.core.dao.RootNodeDao;
import org.yellowbinary.cms.server.core.event.OnLoadEventGenerator;
import org.yellowbinary.cms.server.core.event.ProvidesEventGenerator;
import org.yellowbinary.cms.server.core.model.RootNode;
import org.yellowbinary.cms.server.core.preview.PreviewEventGenerator;
import org.yellowbinary.cms.server.core.preview.Ticket;

import java.util.Collections;
import java.util.Date;

@Service
public class NodeService {

    private static final Logger LOG = LoggerFactory.getLogger(NodeService.class);

    @Autowired
    private PreviewEventGenerator previewEventGenerator;

    @Autowired
    private ProvidesEventGenerator providesEventGenerator;

    @Autowired
    private OnLoadEventGenerator onLoadEventGenerator;

    @Autowired
    private AliasService aliasService;

    @Autowired
    private RootNodeDao rootNodeDao;

    public Node load(String identifier) throws NodeNotFoundException, NodeLoadException, ModuleException {
        return load(identifier, 0);
    }

    public Node load(String identifier, int version) throws NodeNotFoundException, NodeLoadException, ModuleException {
        return loadNode(identifier, version);
    }

    public Node loadNode(String identifier, int version) throws NodeNotFoundException, NodeLoadException, ModuleException {
        LOG.trace("Trying to find alias for [" + identifier + "]");

        String key = aliasService.getKeyForPath(identifier);
        return loadByNodeIdAndVersion(key, version);
    }

    private Node loadByNodeIdAndVersion(String identifier, int version) throws NodeNotFoundException, NodeLoadException, ModuleException {
        RootNode root;
        if (version != 0) {
            root = loadRootNode(identifier, version);
        } else {
            root = loadRootNode(identifier, 0);
        }
        if(root == null) {
            throw new NodeNotFoundException(identifier);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Loaded " + root.toString());
        }

        return decorateNode(root);
    }

    private RootNode loadRootNode(String nodeId, Integer version) throws ModuleException, NodeLoadException, NodeNotFoundException {
        RootNode rootNode;
        // Check for a preview ticket and decorateNode the corresponding rootnode
        Ticket ticket = previewEventGenerator.getValidTicket();
        if (ticket != null) {
            LOG.trace("Preview Ticket found, ignoring version and using preview date of: "+ticket.preview().toString());
            rootNode = rootNodeDao.findByKeyAndPublishedDate(nodeId, ticket.preview().toDate());
        } else {
            //Load RootNode model
            if (version > 0) {
                rootNode = rootNodeDao.findByKeyAndVersion(nodeId, version);
            } else {
                rootNode = rootNodeDao.findByKeyAndPublishedDate(nodeId, new Date());
            }
        }
        return rootNode;
    }

    private Node decorateNode(RootNode rootNode) throws NodeLoadException, ModuleException {

        boolean hasType = StringUtils.isNotBlank(rootNode.getType()) && !rootNode.getType().equals(RootNode.class.getName());

        if (hasType) {
            onLoadEventGenerator.triggerBeforeInterceptor(rootNode, Core.Base.NODE, rootNode.getType(), Collections.emptyMap());
        }

        Node node = null;
        if (hasType) {
            node = providesEventGenerator.triggerInterceptor(rootNode, Core.Base.NODE, rootNode.getType());
        }

        if (hasType) {
            onLoadEventGenerator.triggerAfterInterceptor(node, Core.Base.NODE, rootNode.getType(), Collections.<String, Object>emptyMap());
        }

        return node;
    }

}
