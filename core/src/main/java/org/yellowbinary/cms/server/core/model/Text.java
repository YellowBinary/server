package org.yellowbinary.cms.server.core.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "text")
public class Text {

    public static final String TYPE = "text";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String identifier;

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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Text() {
        this.identifier = UUID.randomUUID().toString();
        this.value = "";
    }

    public Text(String value) {
        this();
        this.value = value;
    }

}
