package org.yellowbinary.cms.server.core.model;

import org.yellowbinary.cms.server.core.AbstractNode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="block")
public class Block extends AbstractNode {

    public static final String TYPE = "yellowbinary.Block";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String key;

    @NotNull
    private String type;

    @NotNull
    private String referenceId;

    public Block() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Integer getVersion() {
        return 0;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    @Override
    public String toString() {
        return "Block{" +
                "key=" + id +
                ", key='" + key + '\'' +
                ", type='" + type + '\'' +
                ", referenceId='" + referenceId + '\'' +
                '}';
    }

}
