package org.yellowbinary.server.basic_security.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yellowbinary.server.basic_security.model.BasicUser;

@Repository
@Transactional
public interface BasicUserDao extends JpaRepository<BasicUser, Long> {

    @Query("select u from BasicUser u where u.email=?1")
    BasicUser findByEmail(String email);
}
