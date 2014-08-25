package org.yellowbinary.server.basic_auth.model;

import com.google.common.collect.Sets;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "authorization_basic")
public class BasicAuthorization {

    public static final String TYPE = "yellowbinary.basic_security.Authorization";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Path can be either an alias, nodeId or part of a url (without host).
     */
    @Column(unique = true)
    private String key;

    @ElementCollection
    @CollectionTable(name = "authorization_basic_roles")
    private Set<String> roles = Sets.newHashSet();

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

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    /*
    public static BasicAuthorization findWithId(long id) {
        try {
            final Query query = JPA.em().createQuery("select bn from "+BasicAuthorization.class.getName()+" bn where bn.id=:id");
            query.setParameter("id", id);
            return (BasicAuthorization) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static BasicAuthorization findWithPath(String path) {
        try {
            final Query query = JPA.em().createQuery("select ba from "+BasicAuthorization.class.getName()+" ba where ba.path=:path");
            query.setParameter("path", path);
            return (BasicAuthorization) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
*/

}
