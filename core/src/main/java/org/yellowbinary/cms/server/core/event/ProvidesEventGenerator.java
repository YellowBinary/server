package org.yellowbinary.cms.server.core.event;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.yellowbinary.cms.server.core.*;
import org.yellowbinary.cms.server.core.annotation.CachedAnnotation;
import org.yellowbinary.cms.server.core.annotation.ReflectionInvoker;
import org.yellowbinary.cms.server.core.context.Context;
import org.yellowbinary.cms.server.core.model.content.Block;
import org.yellowbinary.cms.server.core.model.RootNode;
import org.yellowbinary.cms.server.core.model.content.Text;
import org.yellowbinary.cms.server.core.stereotypes.Provides;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Helper to trigger \@Provides origo interceptors. Should not be used directly except in core and admin, use NodeHelper
 * instead when creating a new module.
 *
 * @see org.yellowbinary.cms.server.core.stereotypes.Provides
 */
@Service
public class ProvidesEventGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(ProvidesEventGenerator.class);

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private InterceptorRepository interceptorRepository;

    @Autowired
    private EventHandlerService eventHandlerService;

    public <T> T triggerInterceptor(RootNode node, String providesType, String withType) throws NodeLoadException, ModuleException {
        return triggerInterceptor(node, providesType, withType, Maps.<String, Object>newHashMap());
    }

    public <T> T triggerInterceptor(RootNode node, String providesType, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException {
        Assert.notNull(Context.current(), "Context can't be NULL");
        CachedAnnotation cachedAnnotation = getCachedAnnotationIfModuleIsEnabled(providesType, withType);
        Context.current().addAttribute(withType, cachedAnnotation.getMethod().getDeclaringClass());
        return ReflectionInvoker.execute(cachedAnnotation, node, withType, args);
    }

/*
    public static <T> T triggerInterceptor(Node node, String providesType, String withType, Navigation navigation, Map<String, Object> args) throws NodeLoadException, ModuleException {
        CachedAnnotation cachedAnnotation = getCachedAnnotationIfModuleIsEnabled(providesType, withType);
        assert(NodeContext.current() != null);
        NodeContext.current().attributes.put(withType, cachedAnnotation.method.getDeclaringClass());
        return ReflectionInvoker.execute(cachedAnnotation, node, withType, navigation, args);
    }

    public static <T> T triggerInterceptor(Node node, String providesType, String withType, Form form, Map<String, Object> args) throws NodeLoadException, ModuleException {
        CachedAnnotation cachedAnnotation = getCachedAnnotationIfModuleIsEnabled(providesType, withType);
        assert(NodeContext.current() != null);
        NodeContext.current().attributes.put(withType, cachedAnnotation.method.getDeclaringClass());
        return ReflectionInvoker.execute(cachedAnnotation, node, withType, form, args);
    }

*/

    public <T> T triggerInterceptor(Node node, String providesType, String withType, Block block, Map<String, Object> args) throws NodeLoadException, ModuleException {
        CachedAnnotation cachedAnnotation = getCachedAnnotationIfModuleIsEnabled(providesType, withType);
        assert(Context.current() != null);
        Context.current().addAttribute(withType, cachedAnnotation.getMethod().getDeclaringClass());
        return ReflectionInvoker.execute(cachedAnnotation, node, withType, block, args);
    }

    public <T> T triggerInterceptor(Node node, String providesType, String withType, Text text, Map<String, Object> args) throws NodeLoadException, ModuleException {
        CachedAnnotation cachedAnnotation = getCachedAnnotationIfModuleIsEnabled(providesType, withType);
        assert(Context.current() != null);
        Context.current().addAttribute(withType, cachedAnnotation.getMethod().getDeclaringClass());
        return ReflectionInvoker.execute(cachedAnnotation, node, withType, text, args);
    }

    private CachedAnnotation getCachedAnnotationIfModuleIsEnabled(String providesType, String withType) throws ModuleException {
        CachedAnnotation cachedAnnotation = findInterceptor(providesType, withType);
        if (!moduleRepository.isEnabled(cachedAnnotation.getModule())) {
            LOG.debug("Module '" + cachedAnnotation.getModule().getName() + "' is disabled");
            throw new ModuleException(cachedAnnotation.getModule().getName(), ModuleException.Cause.NOT_ENABLED);
        }
        return cachedAnnotation;
    }

    /**
     * Filters out the cached providers types.
     * @return a list of all the classes that provides a matching nodeType and a withType
     * @see org.yellowbinary.cms.server.core.stereotypes.Provides
     */
    public Set<CachedAnnotation> getAllProviders(final String type, final String with) {
        return interceptorRepository.getInterceptors(Provides.class, new InterceptorRepository.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cacheAnnotation) {
                Provides annotation = (Provides) cacheAnnotation.getAnnotation();
                return annotation.base().equals(type) && annotation.with().equals(with);
            }
        });
    }

    /**
     * Filters out the cached providers types.
     * @return a list of all the classes that provides a matching nodeType
     * @see Provides
     */
    public Set<CachedAnnotation> getAllProviders(final String type) {
        return interceptorRepository.getInterceptors(Provides.class, new InterceptorRepository.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cacheAnnotation) {
                Provides annotation = (Provides) cacheAnnotation.getAnnotation();
                return annotation.base().equals(type);
            }
        });
    }

    public CachedAnnotation findInterceptor(final String nodeType, final String withType) {
        List<CachedAnnotation> providers = Lists.newArrayList(interceptorRepository.getInterceptors(Provides.class, new InterceptorRepository.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cachedAnnotation) {
                Provides annotation = (Provides) cachedAnnotation.getAnnotation();
                return annotation.base().equals(nodeType) && annotation.with().equals(withType);
            }
        }));

        CachedAnnotation cacheAnnotation = eventHandlerService.selectEventHandler(Provides.class, nodeType, withType, providers);
        if (cacheAnnotation == null) {
            throw new NoSuchProviderException(nodeType, withType, "Every type (specified by using attribute 'with') must have a class annotated with @Provides to instantiate an instance. Unable to find a provider for type '" + nodeType + "' with '"+withType+"'");
        }
        return cacheAnnotation;
    }

}
