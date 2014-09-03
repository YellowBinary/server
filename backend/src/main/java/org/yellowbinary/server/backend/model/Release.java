package org.yellowbinary.server.backend.model;

import javax.persistence.*;
import java.util.Date;

/**
 * A release keeps one or several changes connected so they can be published at the same time.
 */
@Entity
@Table(name = "node_release")
public class Release {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * A name to keep easily recognizable
     */
    @Column(unique = true)
    private String name;

    /**
     * The date this version should be available for public viewing
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date publish;

    /**
     * The date this version should be removed from public viewing
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date unPublish;

    @Enumerated(EnumType.STRING)
    private State state;

    public Release() {
    }

    public Release(State state) {
        this();
        this.state = state;
    }

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

    public Date getPublish() {
        return publish;
    }

    public void setPublish(Date publish) {
        this.publish = publish;
    }

    public Date getUnPublish() {
        return unPublish;
    }

    public void setUnPublish(Date unPublish) {
        this.unPublish = unPublish;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
