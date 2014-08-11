package org.yellowbinary.cms.server.core.model;

import org.yellowbinary.cms.server.core.AbstractNode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "text")
public class Text extends AbstractNode {

    public static final String TYPE = "text";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String key;

    @NotNull
    @Column(name = "content")
    @Lob
    private String value;

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

    @Override
    public Integer getVersion() {
        return 0;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Text() {
        this.key = UUID.randomUUID().toString();
        this.value = "";
    }

    public Text(String value) {
        this();
        this.value = value;
    }

}
