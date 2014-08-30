package org.yellowbinary.server.basic_security.service;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yellowbinary.server.core.dao.ConfigurationDao;
import org.yellowbinary.server.core.security.Security;
import org.yellowbinary.server.core.service.SessionService;

import javax.annotation.PostConstruct;

@Component
public class HeaderAuthenticationUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HeaderAuthenticationUtil.class);

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ConfigurationDao configurationDao;

    private Period tokenMaxAge;

    @PostConstruct
    private void init() {
        tokenMaxAge = getTokenMaxAge();
    }

    public String getUserName() {
        String header = sessionService.get(Security.Header.TOKEN_KEY);
        return StringUtils.isNotBlank(header) ? extractUserName(header) : null;
    }

    private String extractUserName(String value) {

        String[] split = value.split("\\|");
        String username = split[0];
        DateTime timestamp =  new DateTime(Long.parseLong(split[1]));
        if (timestamp.isAfter(DateTime.now().minus(tokenMaxAge))) {
            return username;
        }
        return null;
    }

    public String setUserName(String userName) {
        return sessionService.set(Security.Header.TOKEN_KEY, userName + "|" + System.currentTimeMillis());
    }


    private Period getTokenMaxAge() {
        String maxAge = configurationDao.readValue(String.class, "yellowbinary.auth.maxAge", "2m");
        PeriodFormatter format = new PeriodFormatterBuilder()
                .appendDays()
                .appendSuffix("d", "d")
                .printZeroRarelyFirst()
                .appendHours()
                .appendSuffix("h", "h")
                .printZeroRarelyFirst()
                .appendMinutes()
                .appendSuffix("m", "m")
                .toFormatter();
        Period sessionMaxAge = format.parsePeriod(maxAge);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Session maxAge is: "+
                            formatIfNotZero(sessionMaxAge.getDays(), "days", "day") +
                            formatIfNotZero(sessionMaxAge.getHours(), "hours", "hour") +
                            formatIfNotZero(sessionMaxAge.getMinutes(), "minutes", "minute")
            );
        }
        return sessionMaxAge;
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
