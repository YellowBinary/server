package org.yellowbinary.server.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yellowbinary.server.backend.model.Alias;

import java.util.List;

@Repository
@Transactional
public interface AliasDao extends JpaRepository<Alias, Long> {

    @Query("select a from Alias a where a.path=?1")
    Alias findByPath(String path);

    @Query("select a from Alias a where a.node=?1")
    List<Alias> findByNode(String key);

    /*
        public static Alias findWithId(long key) {
        return JPA.em().find(Alias.class, key);
    }

    public static Alias findWithPath(String path) {
        try {
            final Query query = JPA.em().createQuery("from "+Alias.class.getName()+" an where an.path=:path");
            query.setParameter("path", path);
            return (Alias) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static List<Alias> findWithPageId(String pageId) {
        try {
            final Query query = JPA.em().createQuery("from "+Alias.class.getName()+" an where an.pageId=:pageId");
            query.setParameter("pageId", pageId);
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public static Alias findFirstAliasForPageId(String pageId) {
        Collection<Alias> aliases = Alias.findWithPageId(pageId);
        if (aliases.isEmpty()) {
            return null;
        }
        return aliases.iterator().next();
    }

    */
}
