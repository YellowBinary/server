package org.yellowbinary.server.preview.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.yellowbinary.server.preview.model.BasicTicket;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface BasicTicketDao extends JpaRepository<BasicTicket, Long> {

    @Query("from BasicTicket bt where bt.token=?1")
    public BasicTicket findByToken(String token);

}
