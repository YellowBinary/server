package org.yellowbinary.server.security.basic.interceptors;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.yellowbinary.server.backend.Backend;
import org.yellowbinary.server.backend.Node;
import org.yellowbinary.server.backend.dao.AliasDao;
import org.yellowbinary.server.backend.model.Alias;
import org.yellowbinary.server.backend.security.NodeKey;
import org.yellowbinary.server.backend.security.Security;
import org.yellowbinary.server.core.stereotypes.Interceptor;
import org.yellowbinary.server.core.stereotypes.Provides;
import org.yellowbinary.server.security.basic.dao.BasicAuthorizationDao;
import org.yellowbinary.server.security.basic.dao.BasicUserDao;
import org.yellowbinary.server.security.basic.model.BasicAuthorization;
import org.yellowbinary.server.security.basic.model.BasicRole;
import org.yellowbinary.server.security.basic.model.BasicUser;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Interceptor
public class BasicAuthProvider {

    @Autowired
    private BasicUserDao basicUserDao;

    @Autowired
    private BasicAuthorizationDao basicAuthorizationDao;

    @Autowired
    private AliasDao aliasDao;

    @Provides(base = Backend.Base.SECURITY, with = Security.With.AUTHENTICATION_CURRENT_USER)
    public Authentication getCurrentUser(Node node, Map<String, Object> args) throws Exception {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Provides(base = Backend.Base.SECURITY, with = Security.With.AUTHORIZATION_SUBJECT)
    public UserDetails loadUser(Node node, Map<String, Object> args) throws Exception {

        String username = (String) args.get("username");

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

    @Provides(base = Backend.Base.SECURITY, with = Security.With.AUTHORIZATION_ROLES)
    public Set<String> loadAuthorizedRoles(NodeKey nodeKey, Map<String, Object> args) throws Exception {
        BasicAuthorization basicAuthorization = getBasicAuthorization(nodeKey.getIdentifier());
        if (basicAuthorization != null) {
            return basicAuthorization.getRoles();
        }
        return Collections.emptySet();
    }

    public BasicAuthorization getBasicAuthorization(String path) {
        BasicAuthorization basicAuthorization = null;
        // Try path first
        if (path.contains("/")) {
            basicAuthorization = getBasicAuthorizationForPath(path);
        }
        // Not a path, most likely a key
        if (basicAuthorization == null) {
            basicAuthorization = basicAuthorizationDao.findByKey(path.substring(1));
        }
        // Not a path or an alias, let's try to find an alias
        if (basicAuthorization == null) {
            basicAuthorization = getBasicAuthorizationForAlias(path);
        }

        return basicAuthorization;
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
            currentPath = currentPath.substring(0, currentPath.lastIndexOf('/'));
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
