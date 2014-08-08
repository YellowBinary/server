package org.yellowbinary.cms.server.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yellowbinary.cms.server.core.model.Block;

@Repository
@Transactional
public interface BlockDao extends JpaRepository<Block, Long> {

/*
    public static Block findWithIdentifier(String identifier) {
        //noinspection unchecked
        return (Block) JPA.em().
                createQuery("select distinct s from "+Block.class.getName()+" s where s.identifier = :identifier").
                setParameter("identifier", identifier).
                getSingleResult();
    }

*/

}
