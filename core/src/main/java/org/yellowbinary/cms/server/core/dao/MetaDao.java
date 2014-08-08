package org.yellowbinary.cms.server.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yellowbinary.cms.server.core.model.Meta;

@Repository
@Transactional
public interface MetaDao extends JpaRepository<Meta, Long> {

/*
     public static Meta findWithNodeIdAndSpecificVersion(String key, Integer version, String referenceId) {
        try {
            String queryString = "select distinct m from "+Meta.class.getName()+" m " +
                    "where m.key = :key and m.version = :version and m.referenceId = :referenceId";
            final Query query = JPA.em().createQuery(queryString);
            query.setParameter("version", version).setParameter("key", key).setParameter("referenceId", referenceId);
            return (Meta) query.getSingleResult();
        } catch (NoResultException e) {
            Logger.trace("No Meta found for node '" + key + "' version '" + version + "' with reference '" + referenceId + "'");
            return null;
        }
    }

*/

}
