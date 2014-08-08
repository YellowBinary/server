package org.yellowbinary.cms.server.core.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "alias")
public class Alias {

    public static final String TYPE = "origo.alias";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String path;

    @NotNull
    private String pageId;

    protected Alias() {
    }

    public Alias(String path, String pageId) {
        this();
        this.path = path;
        this.pageId = pageId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    @Override
    public String toString() {
        return new StringBuilder().
                append("Alias {").
                append("path='").append(path).append("\', ").
                append("pageId='").append(pageId).append('\'').
                append('}').
                toString();
    }

}
