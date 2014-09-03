package org.yellowbinary.server.backend;

public class NodeLoadException extends Exception {

    private final String nodeId;

    public NodeLoadException(String nodeId, String message) {
        super(message);
        this.nodeId = nodeId;
    }

    public NodeLoadException(String nodeId, String message, Throwable cause) {
        super(message, cause);
        this.nodeId = nodeId;
    }
}
