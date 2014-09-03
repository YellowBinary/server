package org.yellowbinary.server.backend.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yellowbinary.server.backend.Backend;
import org.yellowbinary.server.backend.Node;
import org.yellowbinary.server.backend.NodeLoadException;
import org.yellowbinary.server.backend.NodeNotFoundException;
import org.yellowbinary.server.core.*;
import org.yellowbinary.server.backend.dao.RootNodeDao;
import org.yellowbinary.server.core.event.OnLoadEventGenerator;
import org.yellowbinary.server.core.event.ProvidesEventGenerator;
import org.yellowbinary.server.backend.model.RootNode;
import org.yellowbinary.server.backend.preview.PreviewEventGenerator;
import org.yellowbinary.server.backend.preview.Ticket;
import org.yellowbinary.server.core.stereotypes.security.ReadAccess;

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

    @ReadAccess
    public Node load(String identifier) throws NodeNotFoundException, NodeLoadException, ModuleException {
        return load(identifier, 0);
    }

    @ReadAccess
    public Node load(String identifier, int version) throws NodeNotFoundException, NodeLoadException, ModuleException {
        LOG.trace(String.format("Trying to find alias for [%s]", identifier));

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
            LOG.debug(String.format("Loaded %s", root));
        }

        return decorateNode(root);
    }

    private RootNode loadRootNode(String nodeId, Integer version) throws ModuleException, NodeLoadException, NodeNotFoundException {
        RootNode rootNode;
        // Check for a preview ticket and decorateNode the corresponding rootnode
        Ticket ticket = previewEventGenerator.getValidTicket();
        if (ticket != null) {
            LOG.trace(String.format("Preview Ticket found, ignoring version and using preview date of: %s", ticket.getPreviewDateTime()));
            rootNode = rootNodeDao.findByKeyAndPublishedDate(nodeId, ticket.getPreviewDateTime().toDate());
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
            onLoadEventGenerator.triggerBeforeInterceptor(rootNode, Backend.Base.NODE, rootNode.getType(), Collections.<String, Object>emptyMap());
        }

        Node node = null;
        if (hasType) {
            node = providesEventGenerator.triggerInterceptor(rootNode, Backend.Base.NODE, rootNode.getType());
        }

        if (hasType) {
            onLoadEventGenerator.triggerAfterInterceptor(node, Backend.Base.NODE, rootNode.getType(), Collections.<String, Object>emptyMap());
        }

        return node;
    }

}
