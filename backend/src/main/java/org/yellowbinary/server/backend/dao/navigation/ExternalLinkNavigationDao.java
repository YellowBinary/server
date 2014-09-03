package org.yellowbinary.server.backend.dao.navigation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yellowbinary.server.backend.model.content.navigation.ExternalLinkNavigation;

@Repository
@Transactional
public interface ExternalLinkNavigationDao extends JpaRepository<ExternalLinkNavigation, Long> {

/*
    public String getLink() {
        return link;
    }

    public static ExternalLinkNavigation findWithIdentifier(String identifier) {
        try {
            return (ExternalLinkNavigation) JPA.em().createQuery("from "+ExternalLinkNavigation.class.getName()+" where identifier=:identifier").
                    setParameter("identifier", identifier).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

*/

}
