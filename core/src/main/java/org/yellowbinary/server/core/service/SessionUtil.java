package org.yellowbinary.server.core.service;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yellowbinary.server.core.context.Context;
import org.yellowbinary.server.core.helpers.DateHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionUtil {

    private static final Logger LOG = LoggerFactory.getLogger(SessionUtil.class);

    public static final String SESSION_KEY = "X-Session-Timestamp";

    private EncryptionUtil encryptionUtil;

    private Period sessionMaxAge;

    public SessionUtil() {
        sessionMaxAge = new Period(30, PeriodType.minutes());
        encryptionUtil = new EncryptionUtil();
    }

    public void validateAndUpdateTimestamp() {
        isSessionValid();
        set(SESSION_KEY, String.valueOf(DateTime.now().getMillis()));
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
        String timestamp = getResponse().getHeader(SESSION_KEY);
        DateTime dateTime = new DateTime(0);
        if (timestamp != null) {
            try {
                dateTime = new DateTime(Long.parseLong(encryptionUtil.decrypt(timestamp)));
            } catch (NumberFormatException ignored) {
            }
        }
        return dateTime;
    }

    public String set(String key, String value) {
        getResponse().setHeader(key, encryptionUtil.encrypt(value));
        return value;
    }

    public String get(String key) {
        if (isSessionValid()) {
            String value = getRequest().getHeader(key);
            if (value != null) {
                return encryptionUtil.decrypt(value);
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

    public SessionUtil encryptionUtil(EncryptionUtil encryptionUtil) {
        this.encryptionUtil = encryptionUtil;
        return this;
    }

    public EncryptionUtil encryptionUtil() {
        return encryptionUtil;
    }

    public SessionUtil sessionMaxAge(Period sessionMaxAge) {
        this.sessionMaxAge = sessionMaxAge;
        return this;
    }

    public SessionUtil sessionMaxAge(String sessionMaxAge) {
        this.sessionMaxAge = DateHelper.parsePeriod(sessionMaxAge);
        LOG.debug(String.format("Session max age is: %s", DateHelper.toString(this.sessionMaxAge)));
        return this;
    }

    public Period sessionMaxAge() {
        return sessionMaxAge;
    }
}
