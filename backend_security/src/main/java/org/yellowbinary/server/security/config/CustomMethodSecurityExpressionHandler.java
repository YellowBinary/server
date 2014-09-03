package org.yellowbinary.server.security.config;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.yellowbinary.server.security.dao.BasicAuthorizationDao;
import org.yellowbinary.server.backend.service.AliasService;

@Component
public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    @Autowired
    private BasicAuthorizationDao basicAuthorizationDao;

    @Autowired
    private AliasService aliasService;

    public CustomMethodSecurityExpressionHandler() {
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        CustomSecurityExpressionRoot root = new CustomSecurityExpressionRoot(authentication);
        root.setThis(invocation.getThis());
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(new AuthenticationTrustResolverImpl());
        root.setRoleHierarchy(getRoleHierarchy());
        root.setMethod(invocation.getMethod());
        root.basicAuthorizationDao(basicAuthorizationDao);
        root.aliasService(aliasService);
        return root;
    }

}