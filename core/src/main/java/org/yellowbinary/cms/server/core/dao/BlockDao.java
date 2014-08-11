package org.yellowbinary.cms.server.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yellowbinary.cms.server.core.model.Block;

@Repository
@Transactional
public interface BlockDao extends JpaRepository<Block, Long> {

    @Query("select distinct b from Block b where b.key = ?1")
    Block findByKeyAndVersion(String key);

}