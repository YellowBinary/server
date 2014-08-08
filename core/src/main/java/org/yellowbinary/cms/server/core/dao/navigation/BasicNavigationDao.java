package org.yellowbinary.cms.server.core.dao.navigation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yellowbinary.cms.server.core.model.navigation.BasicNavigation;

@Repository
@Transactional
public interface BasicNavigationDao extends JpaRepository<BasicNavigation, Long> {

/*
    public static BasicNavigation findWithId(long id) {
        try {
            final Query query = JPA.em().createQuery("select bn from "+BasicNavigation.class.getName()+" bn where bn.id=:id");
            query.setParameter("id", id);
            return (BasicNavigation) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static BasicNavigation findWithReferenceIdentifier(String referenceId) {
        try {
            final Query query = JPA.em().createQuery("select bn from "+BasicNavigation.class.getName()+" bn where bn.referenceId=:reference");
            query.setParameter("reference", referenceId);
            return (BasicNavigation) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static List<BasicNavigation> findWithSectionWithoutParent(String section) {
        final Query query = JPA.em().createQuery("select bn from "+BasicNavigation.class.getName()+" bn where bn.section=:section and parent is null");
        query.setParameter("section", section);
        List<BasicNavigation> resultList = query.getResultList();
        Collections.sort(resultList);
        return resultList;
    }

    public static List<BasicNavigation> findAllWithSection(String section) {
        final Query query = JPA.em().createQuery("select bn from "+BasicNavigation.class.getName()+" bn where bn.section=:section");
        query.setParameter("section", section);
        List<BasicNavigation> resultList = query.getResultList();
        Collections.sort(resultList);
        return resultList;
    }

    public static List<BasicNavigation> findWithSection(String section, BasicNavigation parent) {
        return findWithSection(section, parent.id);
    }

    public static List<BasicNavigation> findWithSection(String section, Long parentId) {
        final Query query = JPA.em().createQuery("select bn from "+BasicNavigation.class.getName()+" bn where bn.section=:section and bn.parent.id=:parentId");
        query.setParameter("section", section);
        query.setParameter("parentId", parentId);
        List<BasicNavigation> resultList = query.getResultList();
        Collections.sort(resultList);
        return resultList;
    }
*/

}
