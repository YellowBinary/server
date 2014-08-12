package org.yellowbinary.cms.server.core.model.content;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.yellowbinary.cms.server.core.AbstractNode;
import org.yellowbinary.cms.server.core.Node;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="block")
@JsonPropertyOrder({ "id", "key", "type", "weight", "referenceId", "regions", "children" })
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

    public Node setKey(String key) {
        this.key = key;
        return this;
    }

    public String getType() {
        return type;
    }

    public Node setType(String type) {
        this.type = type;
        return this;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;

        return !(id != null ? !id.equals(block.id) : block.id != null) &&
                !(key != null ? !key.equals(block.key) : block.key != null) &&
                !(referenceId != null ? !referenceId.equals(block.referenceId) : block.referenceId != null) &&
                !(type != null ? !type.equals(block.type) : block.type != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (referenceId != null ? referenceId.hashCode() : 0);
        return result;
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
