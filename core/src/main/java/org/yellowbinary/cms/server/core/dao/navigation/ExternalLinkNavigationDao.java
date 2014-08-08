package org.yellowbinary.cms.server.core.dao.navigation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ExternalLinkNavigationDao extends JpaRepository<ExternalLinkNavigationDao, Long> {

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
