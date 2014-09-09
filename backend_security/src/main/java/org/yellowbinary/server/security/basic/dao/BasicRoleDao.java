package org.yellowbinary.server.security.basic.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yellowbinary.server.security.basic.model.BasicRole;

@Repository
@Transactional
public interface BasicRoleDao extends JpaRepository<BasicRole, Long> {
}
