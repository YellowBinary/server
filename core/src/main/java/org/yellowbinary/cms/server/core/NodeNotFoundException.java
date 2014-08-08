package org.yellowbinary.cms.server.core;

public class NodeNotFoundException extends Exception {

    public final String nodeId;

    public NodeNotFoundException(String nodeId) {
        this.nodeId = nodeId;
    }

}
