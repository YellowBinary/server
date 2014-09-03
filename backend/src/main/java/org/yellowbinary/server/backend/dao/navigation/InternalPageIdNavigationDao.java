package org.yellowbinary.server.backend.dao.navigation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yellowbinary.server.backend.model.content.navigation.InternalPageIdNavigation;

@Repository
@Transactional
public interface InternalPageIdNavigationDao extends JpaRepository<InternalPageIdNavigation, Long> {

/*
    public String getLink() throws NodeLoadException, ModuleException {
        if (CoreSettingsHelper.getStartPage().equals(pageId)) {
            return CoreSettingsHelper.getBaseUrl();
        }
        Collection<Alias> aliases = Alias.findWithPageId(pageId);
        return getAliasUrl(aliases);
    }

    private String getAliasUrl(Collection<Alias> aliases) throws NodeLoadException, ModuleException {
        if (aliases == null || aliases.isEmpty()) {
            RootNode rootNode = RootNode.findLatestVersionWithNodeId(pageId);
            if (rootNode == null) {
                throw new NodeLoadException(identifier, "The navigation with key='"+identifier+"' references pageId='"+pageId+"' which doesn't exist");
            }
            Node node = ProvidesEventGenerator.triggerInterceptor(rootNode, Core.Type.NODE, rootNode.nodeType());
            if (node == null) {
                throw new NodeLoadException(identifier, "The node with key='"+rootNode.key()+"' is not associated with an instance of type '"+rootNode.nodeType()+"'");
            }
            return CoreSettingsHelper.getBaseUrl() + node.key();
        } else {
            Alias alias = aliases.iterator().next();
            return CoreSettingsHelper.getBaseUrl() + alias.path;
        }
    }


    public static InternalPageIdNavigation findWithIdentifier(String identifier) {
        try {
            return (InternalPageIdNavigation) JPA.em().createQuery("from "+InternalPageIdNavigation.class.getName()+" as n where n.identifier=:identifier").
                    setParameter("identifier", identifier).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

*/

}
