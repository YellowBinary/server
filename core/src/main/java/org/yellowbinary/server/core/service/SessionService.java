package org.yellowbinary.server.core.service;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yellowbinary.server.core.context.Context;
import org.yellowbinary.server.core.dao.ConfigurationDao;
import org.yellowbinary.server.core.helpers.DateHelper;

import javax.annotation.PostConstruct;

@Service
public class SessionService {

    private static final Logger LOG = LoggerFactory.getLogger(SessionService.class);

    @Autowired
    private ConfigurationDao configurationDao;

    @Autowired
    private EncryptionService encryptionService;

    private String encryptedTimestampSessionKey;

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
        encryptedTimestampSessionKey = encryptionService.encrypt("origo.session-timestamp");
    }

    public void setTimestamp() {
        Context.current().getRequest().getSession().setAttribute(encryptedTimestampSessionKey, String.valueOf(DateTime.now().getMillis()));
    }

    public DateTime getTimestamp() {
        String timestamp = (String)Context.current().getRequest().getSession().getAttribute(encryptedTimestampSessionKey);
        if (StringUtils.isNotEmpty(timestamp)) {
            try {
                return new DateTime(Long.parseLong(timestamp));
            } catch (NumberFormatException ignored) {
            }
        }
        return new DateTime(0);
    }

    public void set(String key, String value) {
        Context.current().getRequest().getSession().setAttribute(encryptionService.encrypt(key), encryptionService.encrypt(value));
    }

    public String get(String key) {
        return (String)Context.current().getRequest().getSession().getAttribute(encryptionService.encrypt(key));
    }

    public void remove(String key) {
        Context.current().getRequest().getSession().removeAttribute(encryptionService.encrypt(key));
    }

    public boolean checkAndUpdateTimestamp() {
        DateTime timestamp = getTimestamp();
        if (timestamp.isAfter(DateTime.now().minus(sessionMaxAge))) {
            setTimestamp();
            return true;
        }
        return false;
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
