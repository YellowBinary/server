package org.yellowbinary.server.core.service;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yellowbinary.server.core.context.Context;
import org.yellowbinary.server.core.dao.ConfigurationDao;
import org.yellowbinary.server.core.helpers.DateHelper;
import org.yellowbinary.server.core.security.Security;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class SessionService {

    private static final Logger LOG = LoggerFactory.getLogger(SessionService.class);

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private ConfigurationDao configurationDao;

    private Period sessionMaxAge;

    @PostConstruct
    public void register() {
        String maxAge = configurationDao.readValue(String.class, "application.session.maxAge", "30m");
        sessionMaxAge = DateHelper.parsePeriod(maxAge);
        LOG.debug("Session maxAge is: " +
                        formatIfNotZero(sessionMaxAge.getDays(), "days", "day") +
                        formatIfNotZero(sessionMaxAge.getHours(), "hours", "hour") +
                        formatIfNotZero(sessionMaxAge.getMinutes(), "minutes", "minute")
        );
    }

    public void validateAndUpdateTimestamp() {
        isSessionValid();
        set(Security.Header.SESSION_KEY, String.valueOf(DateTime.now().getMillis()));
    }

    public boolean isSessionValid() {
        String sessionValidKey = "SESSION_VALID";
        if (getRequest().getAttribute(sessionValidKey) != null) {
            return (Boolean)getRequest().getAttribute(sessionValidKey);
        }
        if (getTimestamp().isAfter(DateTime.now().minus(sessionMaxAge))) {
            getRequest().setAttribute(sessionValidKey, Boolean.TRUE);
            return true;
        }
        return false;
    }

    public DateTime getTimestamp() {
        String timestamp = getResponse().getHeader(Security.Header.SESSION_KEY);
        DateTime dateTime = new DateTime(0);
        if (timestamp != null) {
            try {
                dateTime = new DateTime(Long.parseLong(encryptionService.decrypt(timestamp)));
            } catch (NumberFormatException ignored) {
            }
        }
        return dateTime;
    }

    public String set(String key, String value) {
        getResponse().setHeader(key, encryptionService.encrypt(value));
        return value;
    }

    public String get(String key) {
        if (isSessionValid()) {
            String value = getResponse().getHeader(key);
            if (value != null) {
                return encryptionService.encrypt(value);
            }
        }
        return null;
    }

    private HttpServletResponse getResponse() {
        return Context.current().getResponse();
    }

    private HttpServletRequest getRequest() {
        return Context.current().getRequest();
    }

    private String formatIfNotZero(int value, String plural, String singleton) {
        if (value > 0) {
            if (value > 1) {
                return "" + value + " " + plural;
            }
            return "" + value + " " + singleton;
        }
        return "";
    }

}
