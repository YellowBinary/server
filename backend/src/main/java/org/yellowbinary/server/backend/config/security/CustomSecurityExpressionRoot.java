package org.yellowbinary.server.backend.config.security;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.yellowbinary.server.backend.Backend;
import org.yellowbinary.server.backend.security.IdentityUnavailableException;
import org.yellowbinary.server.backend.security.NodeKey;
import org.yellowbinary.server.backend.security.Security;
import org.yellowbinary.server.backend.service.AliasService;
import org.yellowbinary.server.core.InterceptorException;
import org.yellowbinary.server.core.ModuleException;
import org.yellowbinary.server.core.event.ProvidesEventGenerator;

import java.lang.reflect.Method;
import java.util.Set;

public class CustomSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private final Logger LOGGER = LoggerFactory.getLogger(CustomSecurityExpressionRoot.class);

    private Object filterObject;
    private Object returnObject;
    private Object target;
    private Method method;

    private AliasService aliasService;
    private ProvidesEventGenerator providesEventGenerator;

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
    public boolean isAuthorized(Object target, Object permission) throws InterceptorException, ModuleException {

        NodeKey nodeKey;
        try {
            nodeKey = new NodeKey(target);
        } catch (IdentityUnavailableException e) {
            LOGGER.trace(String.format("Target [%s] does not have a key to use in authorization", target.getClass().getSimpleName()));
            return true;
        }

        Set<String> roles = providesEventGenerator.triggerInterceptor(nodeKey, Backend.Base.SECURITY, Security.With.AUTHORIZATION_ROLES);
        return !roles.isEmpty() || hasRole(roles);
    }

    private boolean hasRole(Set<String> roles) {
        for (String role : roles) {
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

    public void providesEventGenerator(ProvidesEventGenerator providesEventGenerator) {
        this.providesEventGenerator = providesEventGenerator;
    }

    public void aliasService(AliasService aliasService) {
        this.aliasService = aliasService;
    }
}