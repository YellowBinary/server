package org.yellowbinary.server.security.basic.model;

import com.google.common.collect.Sets;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.jasypt.hibernate4.type.EncryptedStringType;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@TypeDef(
        name="encryptedString",
        typeClass=EncryptedStringType.class,
        parameters={
                @Parameter(name="encryptorRegisteredName", value="stringEncryptor")
        }
)
@Entity
@Table(name = "user")
public class BasicUser {

    public static final String TYPE = "yellowbinary.basic_security.User";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    @Type(type="encryptedString")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<BasicRole> roles = Sets.newHashSet();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Set<BasicRole> roles) {
        this.roles = roles;
    }

    public Set<BasicRole> getRoles() {
        return roles;
    }

/*
    public static BasicUser findWithId(long id) {
        try {
            final Query query = JPA.em().createQuery("select bn from "+BasicUser.class.getName()+" bn where bn.id=:id");
            query.setParameter("id", id);
            return (BasicUser) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static BasicUser findWithEmail(String email) {
        try {
            final Query query = JPA.em().createQuery("select bn from "+BasicUser.class.getName()+" bn where bn.email=:email");
            query.setParameter("email", email);
            return (BasicUser) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
*/


}
