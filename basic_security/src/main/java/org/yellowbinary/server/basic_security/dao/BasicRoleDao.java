package org.yellowbinary.server.basic_security.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yellowbinary.server.basic_security.model.BasicRole;

@Repository
@Transactional
public interface BasicRoleDao extends JpaRepository<BasicRole, Long> {
}
