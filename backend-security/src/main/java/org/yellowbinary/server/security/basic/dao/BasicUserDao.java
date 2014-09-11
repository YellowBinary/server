package org.yellowbinary.server.security.basic.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yellowbinary.server.security.basic.model.BasicUser;

@Repository
@Transactional
public interface BasicUserDao extends JpaRepository<BasicUser, Long> {

    @Query("select u from BasicUser u where u.email=?1")
    BasicUser findByEmail(String email);
}
