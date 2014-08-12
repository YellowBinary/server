package org.yellowbinary.server.core.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "node", uniqueConstraints = @UniqueConstraint(columnNames = {"key", "version"}))
public final class RootNode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String key;

    @NotNull
    private Integer version;

    @ManyToOne(cascade = CascadeType.ALL)
    private Release release;

    @Column(name = "nodeType")
    private String type;

    protected RootNode() {
    }

    public RootNode(Integer version) {
        this(null, version);
    }

    public RootNode(String key, Integer version) {
        this();
        this.key = key;
        this.version = version;
    }

    public RootNode(Integer version, String type) {
        this(version);
        this.type = type;
    }

    public RootNode(String nodeId, Integer version, String type) {
        this(nodeId, version);
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Release getRelease() {
        return release;
    }

    public void setRelease(Release release) {
        this.release = release;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "Node (" + type + " - " + key + "," + version + ")";
    }

}
