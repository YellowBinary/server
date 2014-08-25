package org.yellowbinary.server.core.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yellowbinary.server.core.dao.AliasDao;
import org.yellowbinary.server.core.model.Alias;

import java.util.List;

@Service
public class AliasService {

    private static final Logger LOG = LoggerFactory.getLogger(AliasService.class);

    @Autowired
    private AliasDao aliasDao;

    public String getAliasForKey(String key, String fallback) {
        List<Alias> aliases = aliasDao.findByNode(key);

        String partialUrl;
        // If multiple aliases are defined we use the first one
        if (aliases.iterator().hasNext()) {
            Alias alias = aliases.iterator().next();
            partialUrl = alias.getPath();
        } else {
            // Defaulting to fallback
            partialUrl = fallback;
        }

        if (StringUtils.isNotBlank(partialUrl)) {
            return partialUrl;
        }
        return partialUrl;
    }

    public String getKeyForPath(String identifier) {
        Alias alias = aliasDao.findByPath(identifier);
        if (alias != null) {
            LOG.debug("Found alias [" + alias.toString() + "]");
            return alias.getNode();
        } else {
            LOG.debug("No Alias found, returning [" + identifier + "] as nodeId");
            return identifier;
        }
    }
}
