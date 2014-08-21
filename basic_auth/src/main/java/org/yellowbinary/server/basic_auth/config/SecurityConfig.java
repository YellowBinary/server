package org.yellowbinary.server.basic_auth.config;

import com.google.common.collect.Lists;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AfterInvocationProvider;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.ExpressionBasedAnnotationAttributeFactory;
import org.springframework.security.access.expression.method.ExpressionBasedPostInvocationAdvice;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.intercept.AfterInvocationProviderManager;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor;
import org.springframework.security.access.method.DelegatingMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.access.prepost.PostInvocationAdviceProvider;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.prepost.PrePostAnnotationSecurityMetadataSource;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.EhCacheBasedAclCache;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.yellowbinary.server.basic_auth.security.CustomAccessDeniedHandler;
import org.yellowbinary.server.basic_auth.security.CustomAuthenticationEntryPoint;
import org.yellowbinary.server.basic_auth.security.CustomAuthenticationSuccessHandler;
import org.yellowbinary.server.basic_auth.security.HeaderAuthenticationFilter;
import org.yellowbinary.server.basic_auth.security.HeaderAuthenticationUtil;
import org.yellowbinary.server.core.service.EncryptionService;
import org.yellowbinary.server.core.service.SessionService;

import javax.sql.DataSource;
import javax.servlet.Filter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String ANONYMOUS_TOKEN = "anonymous";
    public static String ACL_CACHE_NAME = "__ACL_CACHE__";

    @Autowired
    private HeaderAuthenticationUtil headerAuthenticationUtil;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().

                withUser("user").password("password").roles("USER").

                and().

                withUser("admin").password("password").roles("USER", "ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.
                addFilterBefore(authenticationFilter(userDetailsService()), LogoutFilter.class).

                csrf().disable().

                formLogin().successHandler(successHandler()).
                loginProcessingUrl("/login").

                and().

                logout().
                logoutSuccessUrl("/logout").

                and().

                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).

                and().

                exceptionHandling().
                accessDeniedHandler(new CustomAccessDeniedHandler()).
                authenticationEntryPoint(new CustomAuthenticationEntryPoint()).

                and().

                authorizeRequests().

                antMatchers(HttpMethod.POST, "/login").permitAll().
                antMatchers(HttpMethod.POST, "/logout").authenticated().
                antMatchers(HttpMethod.GET, "/**").hasRole("USER").
                antMatchers(HttpMethod.POST, "/**").hasRole("ADMIN").
                antMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN").
                anyRequest().authenticated();

    }

    protected AuthenticationSuccessHandler successHandler() {
        return new CustomAuthenticationSuccessHandler().
                encryptionService(encryptionService).
                headerUtil(headerAuthenticationUtil).
                sessionService(sessionService);
    }

    protected Filter authenticationFilter(UserDetailsService userDetailsService) {
        return new HeaderAuthenticationFilter().
                userDetailsService(userDetailsService).
                headerUtil(headerAuthenticationUtil).
                encryptionService(encryptionService);
    }

/*
    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Bean(name = "cacheManager")
    public CacheManager ehCacheManager() throws IOException {
        EhCacheManagerFactoryBean factory = new EhCacheManagerFactoryBean();
        factory.setCacheManagerName(CacheManager.DEFAULT_NAME);
        factory.setShared(true);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean(name = "ehcache")
    public Ehcache ehCacheFactory() throws CacheException, IOException {
        EhCacheFactoryBean factory = new EhCacheFactoryBean();
        factory.setCacheManager(ehCacheManager());
        factory.setCacheName(ACL_CACHE_NAME);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    public PermissionGrantingStrategy aclPermissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(aclAuditLogger());
    }

    @Bean
    public AclCache aclCache() throws CacheException, IOException {
        return new EhCacheBasedAclCache(ehCacheFactory(), aclPermissionGrantingStrategy(), aclAuthorizationStrategy());
    }

    @Bean
    public AuditLogger aclAuditLogger() {
        return new ConsoleAuditLogger();
    }

    @Bean
    public LookupStrategy aclLookupStrategy() throws CacheException, IOException {
        BasicLookupStrategy lookupStrategy = new BasicLookupStrategy(dataSource, aclCache(), aclAuthorizationStrategy(), aclPermissionGrantingStrategy());
        lookupStrategy.setPermissionFactory(aclPermissionFactory());
        return lookupStrategy;
    }

    @Bean
    public MutableAclService aclService() throws CacheException, IOException {

        JdbcMutableAclService aclService = new JdbcMutableAclService(dataSource, aclLookupStrategy(), aclCache());
        aclService.setClassIdentityQuery("SELECT @@IDENTITY");
        aclService.setSidIdentityQuery("SELECT @@IDENTITY");
        return aclService;
    }


    @Bean(name="expressionHandler")
    public DefaultMethodSecurityExpressionHandler aclExpressionHandler() throws CacheException, IOException {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(aclPermissionEvaluator());
        expressionHandler.setRoleHierarchy(aclRoleHierarchy());
        return expressionHandler;

    }

    @Bean
    public PermissionEvaluator aclPermissionEvaluator() throws CacheException, IOException {
        return new AclPermissionEvaluator(aclService());
    }

    @Bean
    public DefaultPermissionFactory aclPermissionFactory() {
        return new DefaultPermissionFactory();
    }

    @Bean
    public RoleHierarchy aclRoleHierarchy() {
        RoleHierarchyImpl rh = new RoleHierarchyImpl();
        rh.setHierarchy("ROLE_ADMIN > ROLE_USER");
        rh.setHierarchy("ROLE_USER > ROLE_VISITOR");
        return rh;
    }


    @Bean
    public ExpressionBasedAnnotationAttributeFactory expressionBasedAnnotationAttributeFactory() throws CacheException, IOException {
        return new ExpressionBasedAnnotationAttributeFactory(aclExpressionHandler());
    }

    @Bean
    public PrePostAnnotationSecurityMetadataSource prePostAnnotationSecurityMetadataSource() throws CacheException, IOException {
        return new PrePostAnnotationSecurityMetadataSource(expressionBasedAnnotationAttributeFactory());
    }

    @Bean(name = "delegatingMethodSecurityMetadataSource")
    public DelegatingMethodSecurityMetadataSource delegatingMethodSecurityMetadataSource() throws CacheException, IOException {
        List<MethodSecurityMetadataSource> methodSecurityMetadateSources = new ArrayList<MethodSecurityMetadataSource>();
        methodSecurityMetadateSources.add(prePostAnnotationSecurityMetadataSource());

        return new DelegatingMethodSecurityMetadataSource(methodSecurityMetadateSources);
    }

    @Bean
    public ExpressionBasedPreInvocationAdvice expressionBasedPreInvocationAdvice() throws CacheException, IOException {

        ExpressionBasedPreInvocationAdvice advice = new ExpressionBasedPreInvocationAdvice();
        advice.setExpressionHandler(aclExpressionHandler());
        return advice;
    }

    @Bean
    public PreInvocationAuthorizationAdviceVoter preInvocationAuthorizationAdviceVoter() throws CacheException, IOException {
        return new PreInvocationAuthorizationAdviceVoter(expressionBasedPreInvocationAdvice());
    }

    @Bean
    public AccessDecisionManager aclAccessDecisionManager() throws CacheException, IOException {

        List<AccessDecisionVoter> voters = new ArrayList<AccessDecisionVoter>(2);
        voters.add(preInvocationAuthorizationAdviceVoter());
        voters.add(new RoleVoter());
        voters.add(new AuthenticatedVoter());

        return new AffirmativeBased(voters);
    }

    @Bean
    public ExpressionBasedPostInvocationAdvice expressionBasedPostInvocationAdvice() throws CacheException, IOException {
        return new ExpressionBasedPostInvocationAdvice(aclExpressionHandler());
    }

    @Bean
    public PostInvocationAdviceProvider postInvocationAdviceProvider() throws CacheException, IOException {
        return new PostInvocationAdviceProvider(expressionBasedPostInvocationAdvice());
    }

    @Bean
    public AfterInvocationProviderManager afterInvocationProviderManager() throws CacheException, IOException {

        List<AfterInvocationProvider> list = Lists.newArrayList();
        list.add(postInvocationAdviceProvider());

        AfterInvocationProviderManager mgmt = new AfterInvocationProviderManager();
        mgmt.setProviders(list);

        return mgmt;

    }

    @Bean(name = "methodSecurityInterceptor")
    public MethodSecurityInterceptor methodSecurityInterceptor() throws CacheException, IOException {
        MethodSecurityInterceptor interceptor = new MethodSecurityInterceptor();
        interceptor.setAccessDecisionManager(aclAccessDecisionManager());
        interceptor.setAuthenticationManager(aclAuthenticationManager());
        interceptor.setSecurityMetadataSource(delegatingMethodSecurityMetadataSource());
        interceptor.setAfterInvocationManager(afterInvocationProviderManager());
        return interceptor;
    }


    @Bean
    public AnonymousAuthenticationProvider aclAnonymousAuthenticationProvider() {
        return new AnonymousAuthenticationProvider(ANONYMOUS_TOKEN);
    }

    @Bean
    public AuthenticationProvider aclDaoAuthenticationProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService());
        return p;
    }

    @Bean
    public AuthenticationManager aclAuthenticationManager() {
        List<AuthenticationProvider> providers = new ArrayList<AuthenticationProvider>(2);
        providers.add(aclDaoAuthenticationProvider());
        providers.add(aclAnonymousAuthenticationProvider());
        return new ProviderManager(providers);
    }
 */

}