package org.yellowbinary.server.basic_security.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.acls.domain.IdentityUnavailableException;
import org.springframework.security.core.Authentication;
import org.yellowbinary.server.basic_security.dao.BasicAuthorizationDao;
import org.yellowbinary.server.basic_security.model.BasicAuthorization;
import org.yellowbinary.server.basic_security.model.NodeKey;
import org.yellowbinary.server.core.service.AliasService;

import java.lang.reflect.Method;

public class CustomSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private final Logger LOGGER = LoggerFactory.getLogger(CustomSecurityExpressionRoot.class);

    private Object filterObject;
    private Object returnObject;
    private Object target;
    private Method method;

    private AliasService aliasService;
    private BasicAuthorizationDao basicAuthorizationDao;

    public CustomSecurityExpressionRoot(Authentication a) {
        super(a);
    }

    // allow the method to be set
    public void setMethod(Method m) {
        this.method = m;
    }

    // optionally expose the method to be accessed in expressions
    public Method getMethod() {
        return method;
    }

    // create a method that will perform the check with
    // the method name transparently for you
    public boolean isAuthorized(Object target, Object permission) {
        boolean result = true;

        try {
            NodeKey nodeKey = new NodeKey(target);

            BasicAuthorization basicAuthorization = basicAuthorizationDao.findByKey(nodeKey.getIdentifier());
            if (basicAuthorization != null) {
                result = hasRole(basicAuthorization);
            } else {
                String identifier = aliasService.getAliasForKey(nodeKey.getIdentifier(), null);
                if (StringUtils.isNotBlank(identifier)) {
                    basicAuthorization = basicAuthorizationDao.findByKey(identifier);
                    if (basicAuthorization != null) {
                        result = hasRole(basicAuthorization);
                    }
                }
            }

        } catch (IdentityUnavailableException e) {
            LOGGER.trace(String.format("Target [%s] does not have a key to use in authorization", target.getClass().getSimpleName()));
        }

        return result;
    }

    private boolean hasRole(BasicAuthorization basicAuthorization) {
        for (String role : basicAuthorization.getRoles()) {
            if (hasRole(role)) {
                return true;
            }
        }
        return false;
    }

    // implement the interface and provide setters

    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    public Object getFilterObject() {
        return filterObject;
    }

    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    public Object getReturnObject() {
        return returnObject;
    }

    public void setThis(Object target) {
        this.target = target;
    }

    public Object getThis() {
        return target;
    }

    public void basicAuthorizationDao(BasicAuthorizationDao basicAuthorizationDao) {
        this.basicAuthorizationDao = basicAuthorizationDao;
    }

    public void aliasService(AliasService aliasService) {
        this.aliasService = aliasService;
    }
}