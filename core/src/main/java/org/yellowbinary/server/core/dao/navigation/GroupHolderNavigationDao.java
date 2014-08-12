package org.yellowbinary.server.core.dao.navigation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yellowbinary.server.core.model.content.navigation.GroupHolderNavigation;

@Repository
@Transactional
public interface GroupHolderNavigationDao extends JpaRepository<GroupHolderNavigation, Long> {

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
