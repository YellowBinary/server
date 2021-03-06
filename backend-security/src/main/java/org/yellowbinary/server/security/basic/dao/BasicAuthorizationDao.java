package org.yellowbinary.server.security.basic.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yellowbinary.server.security.basic.model.BasicAuthorization;

@Repository
@Transactional
public interface BasicAuthorizationDao extends JpaRepository<BasicAuthorization, Long> {

    @Query("select ba from BasicAuthorization ba where ba.key=?1")
    public BasicAuthorization findByKey(String key);

}
