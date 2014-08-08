package org.yellowbinary.cms.server.core.model.navigation;

import org.yellowbinary.cms.server.core.Navigation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Basic Navigation type. Two type structure. Provides the types ExternalLinkNavigation and InternalPageIdNavigation.
 * Each entity stored represents one of the 3 subtypes. BasicNavigation itself is never used in the UI, only as a common data store for the subtypes.
 *
 * @see ExternalLinkNavigation
 * @see InternalPageIdNavigation
 */
@Entity(name = "basicNavigation")
@Table(name = "navigation_basic")
public class BasicNavigation implements Navigation<BasicNavigation>, Comparable<BasicNavigation> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @ManyToOne
    public BasicNavigation parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    public List<BasicNavigation> children;

    @NotNull
    public String type;

    @NotNull
    public String section;

    @NotNull
    public String referenceId;

    @NotNull
    public int weight;

    public BasicNavigation() {

    }

    @Override
    public String getReferenceId() {
        return referenceId;
    }

    @Override
    public String getSection() {
        return section;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public int compareTo(BasicNavigation navigation) {
        return new Integer(weight).compareTo(navigation.weight);
    }

}
