package org.yellowbinary.server.backend.security;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yellowbinary.server.backend.dao.ConfigurationDao;
import org.yellowbinary.server.core.helpers.DateHelper;
import org.yellowbinary.server.core.service.SessionUtil;

import javax.annotation.PostConstruct;

@Component
public class HeaderAuthenticationUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HeaderAuthenticationUtil.class);

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private ConfigurationDao configurationDao;

    private Period tokenMaxAge;

    @PostConstruct
    private void init() {
        tokenMaxAge = getTokenMaxAge();
    }

    public String getUserName() {
        String header = sessionUtil.get(Security.Header.TOKEN_KEY);
        return StringUtils.isNotBlank(header) ? extractUserName(header) : null;
    }

    private String extractUserName(String value) {

        String[] split = value.split("\\|");
        String username = split[0];
        DateTime timestamp = new DateTime(Long.parseLong(split[1]));
        if (timestamp.isAfter(DateTime.now().minus(tokenMaxAge))) {
            return username;
        }
        return null;
    }

    public String setUserName(String userName) {
        return sessionUtil.set(Security.Header.TOKEN_KEY, userName + "|" + System.currentTimeMillis());
    }


    private Period getTokenMaxAge() {
        String maxAge = configurationDao.readValue(String.class, "yellowbinary.auth.maxAge", "2m");
        Period sessionMaxAge = DateHelper.parsePeriod(maxAge);
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Authentication header max age is: %s", DateHelper.toString(sessionMaxAge)));
        }
        return sessionMaxAge;
    }

}
