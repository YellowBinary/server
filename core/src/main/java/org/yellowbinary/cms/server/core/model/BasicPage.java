package org.yellowbinary.cms.server.core.model;

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
 * @see RootNode
 * @see org.yellowbinary.cms.server.core.interceptors.BasicPageProvider
 */
@Entity
@Table(name = "page_basic", uniqueConstraints = @UniqueConstraint(columnNames = {"parentKey", "parentVersion"}))
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return TYPE;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getBlocks() {
        return blocks;
    }

    public void setBlocks(Set<String> blocks) {
        this.blocks = blocks;
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

