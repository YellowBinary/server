package org.yellowbinary.cms.server.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yellowbinary.cms.server.core.model.Text;

@Repository
@Transactional
public interface TextDao extends JpaRepository<Text, Long> {


}
