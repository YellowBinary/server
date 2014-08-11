package org.yellowbinary.cms.server.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yellowbinary.cms.server.core.model.BasicPage;

import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional
public interface BasicPageDao extends JpaRepository<BasicPage, Long> {

    @Query("select p from BasicPage p where p.key = ?1 and p.version = ?2")
    BasicPage findKeyAndVersion(String key, Integer version);

/*
    public List<BasicPage> findAllCurrentVersions(Date asOfDate) {
        try {
            String queryString = "select p from " + BasicPage.class.getName() + " p " +
                    "where p.key in (" +
                    "select n.key from models.origo.core.RootNode n where n.version = (" +
                    "select max(n2.version) from models.origo.core.RootNode n2 left join n2.release r " +
                    "where n2.key = n.key and " +
                    "(r is null or (r.publish = null or r.publish < :today) and" +
                    "(r.unPublish = null or r.unPublish >= :today)" +
                    ")))";
            final Query query = JPA.em().createQuery(queryString);
            query.setParameter("today", asOfDate);
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public BasicPage findPublishedVersion(String key, Date asOfDate) {
        try {
            return (BasicPage) JPA.em().createQuery(
                    "select p from " + BasicPage.class.getName() + " p " +
                            "where p.key = :key and p.key in (" +
                            "select n.key from models.origo.core.RootNode n where n.version = (" +
                            "select max(n2.version) from models.origo.core.RootNode n2 left join n2.release r " +
                            "where n2.key = n.key and " +
                            "(r is null or " +
                            "(r.publish = null or r.publish < :today) and" +
                            "(r.unPublish = null or r.unPublish >= :today)" +
                            "))"
            ).setParameter("key", key).setParameter("today", asOfDate).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public BasicPage findLatestPublishedVersion(String key) {
        return findPublishedVersion(key, new Date());
    }

    public BasicPage findLatestVersion(String key) {
        try {
            String queryString = "select p from " + BasicPage.class.getName() + " p " +
                    "where p.key = :key and p.version = (" +
                    "select max(n.version) from models.origo.core.RootNode n " +
                    "where n.key = p.key" +
                    ")";
            final Query query = JPA.em().createQuery(queryString);
            query.setParameter("key", key);
            return (BasicPage) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public BasicPage findByKeyAndVersion(String key, Integer version) {
        try {
            String queryString = "select p from " + BasicPage.class.getName() + " p " +
                    "where p.key = :key and p.version = :version";
            final Query query = JPA.em().createQuery(queryString);
            query.setParameter("key", key);
            query.setParameter("version", version);
            return (BasicPage) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<BasicPage> findAllLatestVersions() {
        try {
            String queryString = "select p from " + BasicPage.class.getName() + " p " +
                    "where p.version = (" +
                    "select max(n.version) from models.origo.core.RootNode n " +
                    "where n.key = p.key" +
                    ")";
            final Query query = JPA.em().createQuery(queryString);
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
*/



}
