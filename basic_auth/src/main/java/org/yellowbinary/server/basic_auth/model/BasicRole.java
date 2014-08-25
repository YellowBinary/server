package org.yellowbinary.server.basic_auth.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class BasicRole {

    public static final String TYPE = "yellowbinary.basic_security.Role";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

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

    /*
    public static BasicRole findWithId(long id) {
        try {
            final Query query = JPA.em().createQuery("select bn from "+BasicRole.class.getName()+" bn where bn.id=:id", BasicRole.class);
            query.setParameter("id", id);
            return (BasicRole) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static List<BasicRole> list() {
        try {
            final Query query = JPA.em().createQuery("select bn from "+BasicRole.class.getName()+" bn", BasicRole.class);
            //noinspection unchecked
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
*/

}
