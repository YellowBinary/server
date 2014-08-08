package org.yellowbinary.cms.server.core.dao.navigation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface GroupHolderNavigationDao extends JpaRepository<GroupHolderNavigationDao, Long> {

/*
    public static GroupHolderNavigation findWithIdentifier(String identifier) {
        try {
            return (GroupHolderNavigation) JPA.em().createQuery("from "+GroupHolderNavigation.class.getName()+" where identifier=:identifier").
                    setParameter("identifier", identifier).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }
*/

}
