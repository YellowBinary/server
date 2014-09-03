package org.yellowbinary.server.preview.service;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.yellowbinary.server.backend.Backend;
import org.yellowbinary.server.backend.NodeLoadException;
import org.yellowbinary.server.core.ModuleException;
import org.yellowbinary.server.core.context.Context;
import org.yellowbinary.server.backend.dao.ConfigurationDao;
import org.yellowbinary.server.core.event.ProvidesEventGenerator;
import org.yellowbinary.server.core.helpers.DateHelper;
import org.yellowbinary.server.backend.security.Security;
import org.yellowbinary.server.core.service.SessionService;
import org.yellowbinary.server.preview.dao.BasicTicketDao;
import org.yellowbinary.server.preview.model.BasicTicket;

import javax.persistence.NoResultException;
import java.util.UUID;

@Service
public class PreviewService {

    private static final String SESSION_PREVIEW_TICKET_KEY = "yellowbinary.session.preview_ticket";

    @Autowired
    private ConfigurationDao configurationDao;

    @Autowired
    private BasicTicketDao basicTicketDao;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ProvidesEventGenerator providesEventGenerator;

    private static Period previewTicketPeriod;

    public BasicTicket findWithToken(String token) {
        try {
            return basicTicketDao.findByToken(token);
        } catch (NoResultException e) {
            return null;
        }
    }

    public BasicTicket getIfValid(Authentication authentication, String token) {
        assert(authentication != null);
        BasicTicket basicTicket = findWithToken(token);
        if (basicTicket != null) {
            if (basicTicket.getUserId().equals(authentication.getPrincipal()) && isValid(basicTicket)) {
                basicTicket.setValidUntil(updateValidUntil().getMillis());
                return basicTicket;
            }
            basicTicketDao.delete(basicTicket);
        }
        return null;
    }

    public BasicTicket createInstance(Authentication authentication, DateTime preview) {
        assert(authentication != null);
        BasicTicket basicTicket = new BasicTicket();
        basicTicket.setValidUntil(updateValidUntil().getMillis());
        basicTicket.setToken(UUID.randomUUID().toString());
        basicTicket.setUserId((String)authentication.getPrincipal());
        basicTicket.setPreview(preview.getMillis());
        return basicTicketDao.save(basicTicket);
    }

    public boolean isValid(BasicTicket ticket) {
        Period previewPeriod = getPreviewTicketPeriod();
        return new DateTime(ticket.getValidUntil()).isAfter(DateTime.now(DateTimeZone.UTC).minus(previewPeriod));
    }

    public DateTime updateValidUntil() {
        Period previewPeriod = getPreviewTicketPeriod();
        return DateTime.now(DateTimeZone.UTC).plus(previewPeriod);
    }

    public Period getPreviewTicketPeriod() {
        if (previewTicketPeriod == null) {
            String period = configurationDao.readValue(String.class, "yellowbinary.ticket.period");
            if (StringUtils.isNotBlank(period)) {
                previewTicketPeriod = DateHelper.parsePeriod(period);
            } else {
                previewTicketPeriod = Period.minutes(30);
            }
        }
        return previewTicketPeriod;
    }


    public boolean hasTicket() {
        return sessionService.get(SESSION_PREVIEW_TICKET_KEY) != null;
    }

    public boolean verifyCurrent() throws NodeLoadException, ModuleException {
        return getCurrent() != null;
    }

    public BasicTicket getCurrent() throws NodeLoadException, ModuleException {
        String previewToken = sessionService.get(SESSION_PREVIEW_TICKET_KEY);
        if (StringUtils.isNotBlank(previewToken)) {
            Authentication authentication = providesEventGenerator.triggerInterceptor(null, Backend.Base.SECURITY, Security.With.AUTHENTICATION_CURRENT_USER);
            if (authentication != null) {
                return getIfValid(authentication, previewToken);
            }
        }
        return null;
    }

    public BasicTicket updateTicket(DateTime preview) throws ModuleException, NodeLoadException {
        BasicTicket basicTicket = getCurrent();
        if (basicTicket != null) {
            basicTicket.setPreview(preview.getMillis());
            return basicTicketDao.save(basicTicket);
        }
        return null;
    }

    public BasicTicket createNewTicket(DateTime preview) {
        Authentication authentication = (Authentication) Context.current().getAttribute(Security.Params.AUTH_USER);
        if (authentication != null) {
            BasicTicket basicTicket = createInstance(authentication, preview);
            sessionService.set(SESSION_PREVIEW_TICKET_KEY, basicTicket.getToken());
            return basicTicket;
        }
        return null;
    }

}
