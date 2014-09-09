package org.yellowbinary.server.backend.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name = "configuration")
@Table(name = "configuration")
public class ConfigurationValue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
