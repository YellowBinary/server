package org.yellowbinary.server.basic_auth.service;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.yellowbinary.server.basic_auth.dao.BasicAuthorizationDao;
import org.yellowbinary.server.basic_auth.dao.BasicUserDao;
import org.yellowbinary.server.basic_auth.model.BasicAuthorization;
import org.yellowbinary.server.basic_auth.model.BasicRole;
import org.yellowbinary.server.basic_auth.model.BasicUser;
import org.yellowbinary.server.core.dao.AliasDao;
import org.yellowbinary.server.core.model.Alias;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BasicUserDetailsService implements UserDetailsService {

    @Autowired
    private BasicUserDao basicUserDao;

    @Autowired
    private BasicAuthorizationDao basicAuthorizationDao;

    @Autowired
    private AliasDao aliasDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        BasicUser user = basicUserDao.findByEmail(username);

        if (user != null) {
            Set<BasicRole> roles = user.getRoles();
            List<GrantedAuthority> authorities = Lists.newArrayList();
            for (BasicRole role : roles) {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }
            return new User(username, user.getPassword(), authorities);
        }
        throw new UsernameNotFoundException(String.format("User '%s' not found", username));
    }

    public BasicAuthorization getBasicAuthorization(String path) {
        BasicAuthorization basicAuthorization = getBasicAuthorizationForPath(path);
        if (basicAuthorization != null) {
            return basicAuthorization;
        }

        return getBasicAuthorizationForAlias(path);
    }

    protected BasicAuthorization getBasicAuthorizationForPath(String path) {
        // Try this as a path
        BasicAuthorization basicAuthorization = selectLongestMatchingPath(path);
        if (basicAuthorization != null) {
            return basicAuthorization;
        }

        // Not a path, most likely a node id so strip leading '/' and try again
        if (path.startsWith("/")) {
            return basicAuthorizationDao.findByKey(path.substring(1));
        }
        return null;
    }

    public BasicAuthorization selectLongestMatchingPath(String path) {
        BasicAuthorization basicAuthorization;
        String currentPath = path;
        do {
            basicAuthorization = basicAuthorizationDao.findByKey(currentPath);
            if (basicAuthorization != null) {
                return basicAuthorization;
            }
            currentPath = currentPath.substring(0, currentPath.lastIndexOf("/"));
        } while(StringUtils.isNotBlank(currentPath));
        return null;
    }

    private boolean matches(String queryPath, String authPath) {
        Pattern pattern = Pattern.compile(authPath + ".*");
        Matcher matcher = pattern.matcher(queryPath);
        return matcher.matches();
    }

    protected BasicAuthorization getBasicAuthorizationForAlias(String path) {
        Alias alias = getAlias(path);
        if (alias != null) {
            return basicAuthorizationDao.findByKey(alias.getNode());
        }
        return null;
    }

    protected Alias getAlias(String path) {
        Alias alias = aliasDao.findByPath(path);
        if (alias != null ) {
            return alias;
        }
        if (path.startsWith("/")) {
            return aliasDao.findByPath(path.substring(1));
        }
        return null;
    }

}
