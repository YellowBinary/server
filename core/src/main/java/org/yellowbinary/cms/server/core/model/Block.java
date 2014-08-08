package org.yellowbinary.cms.server.core.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="block")
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String identifier;

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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
                "id=" + id +
                ", identifier='" + identifier + '\'' +
                ", type='" + type + '\'' +
                ", referenceId='" + referenceId + '\'' +
                '}';
    }

}
