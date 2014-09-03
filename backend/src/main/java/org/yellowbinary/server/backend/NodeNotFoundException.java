package org.yellowbinary.server.backend;

public class NodeNotFoundException extends RuntimeException {

    public final String nodeId;

    public NodeNotFoundException(String nodeId) {
        this.nodeId = nodeId;
    }

}
