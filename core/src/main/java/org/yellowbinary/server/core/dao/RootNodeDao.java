package org.yellowbinary.server.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yellowbinary.server.core.model.RootNode;

import java.awt.print.Pageable;
import java.util.Date;

@Repository
@Transactional
public interface RootNodeDao extends JpaRepository<RootNode, Long> {

    @Query("select distinct rn from RootNode rn left join rn.release r " +
            "where rn.key = ?1 and (r = null or (r.state = 'PUBLISHED' and (r.publish = null or r.publish < ?2) and " +
            "(r.unPublish = null or r.unPublish >= ?2))) order by rn.version desc")
    RootNode findByKeyAndPublishedDate(String key, Date date);

    @Query("select rn from RootNode rn where rn.key = ?1 and rn.version = ?2")
    RootNode findByKeyAndVersion(String key, Integer version);

/*
    @Query("select distinct n from RootNode n where n.key = ?1 order by n.version desc")
    RootNode findLatestVersion(String key, Pageable pageable);

    @Query("select distinct n from RootNode n where n.key = ?1 order by n.version desc")
    RootNode findAllVersions(String key);
*/


/*
    private static void initializeNode(RootNode node) {
        node.elements = Maps.newHashMap();
        node.elements.put(HEAD, Lists.<Element>newArrayList());
        node.headElement = Maps.newHashMap();
        node.tailElement = Maps.newHashMap();
    }

    private static void initializeNodes(Collection<RootNode> nodes) {
        for (RootNode node : nodes) {
            initializeNode(node);
        }
    }

    public static List<RootNode> findCurrentPublishedVersions() {
        return findCurrentPublishedVersionsWithDate(new Date());
    }

    public static List<RootNode> findCurrentVersions() {
        try {
            String queryString = "select distinct n from "+RootNode.class.getName()+" n " +
                    "where n.version = (" +
                    "select max(n2.version) from "+RootNode.class.getName()+" n2 " +
                    "where n2.key = n.key" +
                    ")";
            final Query query = JPA.em().createQuery(queryString);
            List<RootNode> nodes = query.getResultList();
            initializeNodes(nodes);
            return nodes;
        } catch (NoResultException e) {
            return Collections.emptyList();
        }

    }

    public static List<RootNode> findCurrentPublishedVersionsWithDate(Date today) {
        try {
            String queryString = "select n from "+RootNode.class.getName()+" n " +
                    "where n.version = (" +
                    "select max(n2.version) from "+RootNode.class.getName()+" n2 " +
                    "where n2.key = n.key and " +
                    "(n2.release.publish = null or n2.release.publish < :today) and" +
                    "(n2.release.unPublish = null or n2.release.unPublish >= :today)" +
                    ")";
            final Query query = JPA.em().createQuery(queryString);
            query.setParameter("today", today);
            List<RootNode> nodes = query.getResultList();
            initializeNodes(nodes);
            return nodes;
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public static RootNode findKeyAndVersion(String key, Integer version) {
        try {
            String queryString = "select n from "+RootNode.class.getName()+" n " +
                    "where n.key = :key and n.version = :version";
            final Query query = JPA.em().createQuery(queryString);
            query.setParameter("key", key);
            query.setParameter("version", version);
            RootNode node = (RootNode) query.getSingleResult();
            if (node != null) {
                initializeNode(node);
            }
            return node;
        } catch (NoResultException e) {
            return null;
        }
    }

    public static RootNode findLatestPublishedVersionWithNodeId(String key) {
        return findPublishedVersionWithNodeIdAndDate(key, new Date());
    }

    public static RootNode findPublishedVersionWithNodeIdAndDate(String key, Date today) {
        try {
            String queryString = "select distinct n from "+RootNode.class.getName()+" n left join n.release r " +
                    "where n.key = :key and " +
                    "(r = null or (r.state = :state and (r.publish = null or r.publish < :today) and " +
                    "(r.unPublish = null or r.unPublish >= :today))) " +
                    "order by n.version desc";
            final Query query = JPA.em().createQuery(queryString);
            query.setParameter("key", key);
            query.setParameter("today", today);
            query.setParameter("state", State.PUBLISHED);
            List<RootNode> nodes = query.getResultList();
            if (nodes.isEmpty()) {
                return null;
            }
            RootNode node = nodes.get(0);
            initializeNode(node);
            return node;
        } catch (NoResultException e) {
            return null;
        }
    }

    public static RootNode findLatestVersionWithNodeId(String key) {
        try {
            String queryString = "select distinct n from "+RootNode.class.getName()+" n " +
                    "where n.key = :key " +
                    "order by n.version desc";
            final Query query = JPA.em().createQuery(queryString);
            query.setParameter("key", key);
            List<RootNode> nodes = query.getResultList();
            if (nodes.isEmpty()) {
                return null;
            }
            RootNode node = nodes.get(0);
            initializeNode(node);
            return node;
        } catch (NoResultException e) {
            return null;
        }
    }

    public static List<RootNode> findAllVersionsWithNodeId(String key) {
        try {
            String queryString = "select distinct n from "+RootNode.class.getName()+" n " +
                    "where n.key = :key " +
                    "order by n.version desc";
            final Query query = JPA.em().createQuery(queryString);
            query.setParameter("key", key);
            List<RootNode> nodes = query.getResultList();
            initializeNodes(nodes);
            return nodes;
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

*/

}
