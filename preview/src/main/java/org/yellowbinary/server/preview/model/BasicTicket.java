package org.yellowbinary.server.preview.model;

import org.joda.time.DateTime;
import org.yellowbinary.server.backend.preview.Ticket;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ticket")
public class BasicTicket implements Ticket {

    public static final String TYPE = "yellowbinary.Ticket";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String userId;

    @NotNull
    private String token;

    @NotNull
    private long validUntil;

    @NotNull
    private long preview;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(long validUntil) {
        this.validUntil = validUntil;
    }

    public long getPreview() {
        return preview;
    }

    public void setPreview(long preview) {
        this.preview = preview;
    }

    public DateTime getValidUntilDateTime() {
        if (validUntil > 0) {
            return new DateTime().withMillis(validUntil);
        }
        return null;
    }

    public DateTime getPreviewDateTime() {
        if (preview > 0) {
            return new DateTime().withMillis(preview);
        }
        return null;
    }
}
