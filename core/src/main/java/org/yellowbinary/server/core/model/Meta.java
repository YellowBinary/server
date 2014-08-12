package org.yellowbinary.server.core.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "meta")
public class Meta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String key;

    @NotNull
    private Integer version;

    @NotNull
    private String referenceId;

    @NotNull
    private Integer weight;

    @NotNull
    private String region;

    public Meta() {
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

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    // TODO: Make this loaded from the database instead
    public static Meta defaultMeta() {
        Meta meta = new Meta();
        meta.region = "main";
        meta.weight = 100;
        return meta;
    }

}
