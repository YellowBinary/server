package org.yellowbinary.cms.server.core.model.content;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.collect.Sets;
import org.yellowbinary.cms.server.core.AbstractNode;
import org.yellowbinary.cms.server.core.Node;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * The basic type for a page. Directly linked to a RootNode, both it's version and key.
 *
 * @see Node
 * @see org.yellowbinary.cms.server.core.model.RootNode
 * @see org.yellowbinary.cms.server.core.interceptors.BasicPageProvider
 */
@Entity
@Table(name = "page_basic", uniqueConstraints = @UniqueConstraint(columnNames = {"parentKey", "parentVersion"}))
@JsonPropertyOrder({ "id", "key", "version", "type", "weight", "title", "blocks", "regions", "children" })
public class BasicPage extends AbstractNode {

    public static final String TYPE = "yellowbinary.Basicpage";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "parentKey")
    private String key;

    @NotNull
    @Column(name = "parentVersion")
    private Integer version;

    @NotNull
    private String title;

    // Contains a list of identifiers for blocks
    @ElementCollection
    @CollectionTable(name = "page_basic_blocks")
    private Set<String> blocks = Sets.newHashSet();

    public BasicPage() {
    }

    public Long getId() {
        return id;
    }

    public Node setId(Long id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return this.key;
    }

    public Node setKey(String key) {
        this.key = key;
        return this;
    }

    public String getType() {
        return TYPE;
    }

    public Node setType(String type) {
        return this;
    }

    public Integer getVersion() {
        return version;
    }

    public Node setVersion(Integer version) {
        this.version = version;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public BasicPage setTitle(String title) {
        this.title = title;
        return this;
    }

    public Set<String> getBlocks() {
        return blocks;
    }

    public Node setBlocks(Set<String> blocks) {
        this.blocks = blocks;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicPage basicPage = (BasicPage) o;

        return !(blocks != null ? !blocks.equals(basicPage.blocks) : basicPage.blocks != null) &&
                !(id != null ? !id.equals(basicPage.id) : basicPage.id != null) &&
                !(key != null ? !key.equals(basicPage.key) : basicPage.key != null) &&
                !(title != null ? !title.equals(basicPage.title) : basicPage.title != null) &&
                !(version != null ? !version.equals(basicPage.version) : basicPage.version != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (blocks != null ? blocks.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BasicPage{" +
                "key=" + id +
                ", key='" + key + '\'' +
                ", version=" + version +
                ", title='" + title + '\'' +
                ", blocks=" + blocks +
                '}';
    }

}

