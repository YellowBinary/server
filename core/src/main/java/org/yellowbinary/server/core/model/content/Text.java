package org.yellowbinary.server.core.model.content;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.yellowbinary.server.core.AbstractNode;
import org.yellowbinary.server.core.Node;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "text")
@JsonPropertyOrder({ "id", "key", "type", "weight", "value", "regions", "children" })
public class Text extends AbstractNode {

    public static final String TYPE = "yellowbinary.text";

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

    public Text() {
        this.key = UUID.randomUUID().toString();
        this.value = "";
    }

    public Text(String value) {
        this();
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Node setKey(String key) {
        this.key = key;
        return this;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public Node setType(String type) {
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean isChildrenAllowed() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Text text = (Text) o;

        return !(id != null ? !id.equals(text.id) : text.id != null) &&
                key.equals(text.key) &&
                !(value != null ? !value.equals(text.value) : text.value != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + key.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
