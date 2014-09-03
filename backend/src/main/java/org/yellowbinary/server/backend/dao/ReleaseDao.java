package org.yellowbinary.server.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yellowbinary.server.backend.model.Release;

@Repository
@Transactional
public interface ReleaseDao extends JpaRepository<Release, Long> {

    @Query("select r from Release r where r.name = ?1")
    Release findWithName(String name);

/*
    public static Release findWithName(String name) {
        try {
            return (Release) JPA.em().
                    createQuery("select r from " + Release.class.getName() + " r where r.name = :name").
                    setParameter("name", name).
                    getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static List<Release> findAllUnpublished() {
        try {
            //noinspection unchecked
            return (List<Release>)JPA.em().
                    createQuery("select r from " + Release.class.getName() + " r " +
                            "where r.publish = null or r.publish < :today and" +
                            "r.unPublish = null or r.unPublish >= :today)").
                    setParameter("today", new Date()).
                    getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public static List<Release> findAll() {
        try {
            //noinspection unchecked
            return (List<Release>)JPA.em().
                    createQuery("select r from " + Release.class.getName() + " r ").
                    getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
*/


}
