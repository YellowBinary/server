package org.yellowbinary.server.core.annotation;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.yellowbinary.server.core.InitializationException;
import org.yellowbinary.server.core.InterceptorRepository;
import org.yellowbinary.server.core.ModuleRepository;
import org.yellowbinary.server.core.stereotypes.Interceptor;
import org.yellowbinary.server.core.stereotypes.Module;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class AnnotationProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationProcessor.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private InterceptorRepository interceptorRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @PostConstruct
    public void init() throws BeansException {
        interceptorRepository.invalidate();
        scanModules();
        scanModuleSuppliedAnnotations();
        initModules();
        if (LOG.isDebugEnabled()) {

            StringBuilder sb = new StringBuilder();
            for (CachedModule cachedModule : moduleRepository.getAll()) {
                sb.append(" - ").append(cachedModule).append("\n");
            }
            LOG.debug(String.format("Modules registered: %s\n%s", moduleRepository.getAll().size(), sb.toString()));

            sb = new StringBuilder();
            int count = 0;
            Map<Class<? extends Annotation>, Set<CachedAnnotation>> interceptorMap = interceptorRepository.getInterceptorMap();
            for (Class<? extends Annotation> a : interceptorMap.keySet()) {
                Set<CachedAnnotation> interceptors = interceptorMap.get(a);
                sb.append(" - ").append(a.getName()).append(" ").append(interceptors.size()).append("\n");
                count += interceptors.size();
            }
            LOG.debug(String.format("Interceptors registered: %s\n%s", count, sb.toString()));

        }
    }

    private void scanModules() {

        final List<Object> moduleBeans = getSortedModuleBeans();

        // First pass: Add all modules
        for (Object o : moduleBeans) {
            //noinspection unchecked
            moduleRepository.add(o.getClass().getAnnotation(Module.class), o);
        }
        // Second pass: Verify dependencies
        for (Object o : moduleBeans) {
            Module moduleAnnotation = o.getClass().getAnnotation(Module.class);
            assertModuleDependencies(moduleRepository.getModule(moduleAnnotation.name()));
        }
    }

    private void initModules() {
        // Third pass: Init all modules
        for (CachedModule cachedModule : moduleRepository.getAll()) {
            try {
                if (cachedModule.getInitMethod() != null) {
                    cachedModule.getInitMethod().invoke(cachedModule.getBean());
                }
            } catch (InvocationTargetException e) {
                if (e.getCause() != null) {
                    if (e.getCause() instanceof InitializationException) {
                        throw (InitializationException) e.getCause();
                    }
                    throw new InitializationException("Unable to init module", e.getCause());
                }
                throw new InitializationException("Unable to init module", e);
            } catch (IllegalAccessException e) {
                throw new InitializationException("Unable to init module", e);
            }
        }
    }

    private void scanModuleSuppliedAnnotations() {

        Map<Class<? extends Annotation>, List<Prototype>> annotationPrototypes = Maps.newHashMap();

        for (CachedModule module : moduleRepository.getAll()) {

            LOG.trace("------------------------------------------------");
            LOG.trace(String.format("- Processing module '%s'", module.getName()));
            LOG.trace("------------------------------------------------");

            Map<String, Object> interceptors = applicationContext.getBeansWithAnnotation(Interceptor.class);

            try {
                if (module.getAnnotationsMethod() != null) {
                    //noinspection unchecked
                    List<Prototype> prototypes = (List<Prototype>) module.getAnnotationsMethod().invoke(module.getBean());
                    for (Prototype prototype : prototypes) {
                        if (!annotationPrototypes.containsKey(prototype.getAnnotation())) {
                            annotationPrototypes.put(prototype.getAnnotation(), Lists.<Prototype>newArrayList());
                        }
                        List<Prototype> list = annotationPrototypes.get(prototype.getAnnotation());
                        list.add(prototype);
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new InitializationException("Unable to get annotations from module", e);
            }

            for (Class<? extends Annotation> annotation : annotationPrototypes.keySet()) {
                scanEventHandlers(module, interceptors, annotation, annotationPrototypes.get(annotation));
            }

        }
    }

    public void scanEventHandlers(CachedModule module, Map<String, Object> beans, Class<? extends Annotation> annotationClass, List<Prototype> prototypes) {

        LOG.trace(String.format("- - Processing [%s]", annotationClass.getSimpleName()));

        Set<Method> unmatchedMethods = Sets.newHashSet();

        // First pass to find methods matching a prototype
        for (Object bean : beans.values()) {

            Set<Method> methods = Reflections.getAllMethods(bean.getClass(), ReflectionUtils.withAnnotation(annotationClass));
            for (Method m : methods) {

                // Basic sanity checks
                if (Modifier.isStatic(m.getModifiers())) {
                    throw new InitializationException("Method '" + m.getDeclaringClass() + "." + m.getName() +
                            "' is annotated with '" + annotationClass.getName() +
                            "' but the method is static");
                }
                if (Modifier.isPrivate(m.getModifiers()) || Modifier.isProtected(m.getModifiers())) {
                    throw new InitializationException("Method '" + m.getDeclaringClass() + "." + m.getName() +
                            "' is annotated with '" + annotationClass.getName() +
                            "' but the method is not public");
                }

                boolean matched = false;
                // Check if parameters and return type matches
                for (Prototype prototype : prototypes) {
                    if (matchPrototype(prototype, m)) {
                        interceptorRepository.add(module, m.getAnnotation(annotationClass), m, bean);
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                    unmatchedMethods.add(m);
                }
            }
        }

        // Second pass to give some more information about what methods failed
        if (!unmatchedMethods.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Method m : unmatchedMethods) {
                for (Prototype prototype : prototypes) {
                    // Check parameters first
                    Class[] parameterTypes = prototype.getExpectedParameterTypes();
                    Class[] pc = m.getParameterTypes();
                    if (pc.length != parameterTypes.length) {
                        sb.append(String.format("Method '%s.%s (%s)' is annotated with '%s' but the method does not match the required signature but the method does not match the required signature but the method does not match the required signature (different amount of parameters)",
                                m.getDeclaringClass().getName(), m.getName(), StringUtils.join(StringUtils.join(m.getParameterTypes(), ", ")), annotationClass.getName()));
                        break;
                    }
                    for (int i = 0; i < pc.length; i++) {
                        //noinspection unchecked
                        if (!parameterTypes[i].isAssignableFrom(pc[i])) {
                            sb.append(String.format("Method '%s.%s (%s)' is annotated with '%s' but the method does not match the required signature but the method does not match the required signature (parameter '%s' has the wrong type)",
                                    m.getDeclaringClass().getName(), m.getName(), StringUtils.join(StringUtils.join(m.getParameterTypes(), ", ")), annotationClass.getName(), parameterTypes[i].getName()));
                            break;
                        }
                    }
                    // Parameters match so check return type
                    Class returnType = prototype.getExpectedReturnType();
                    //noinspection unchecked
                    if (returnType != null && !returnType.isAssignableFrom(m.getReturnType())) {
                        sb.append(String.format("Method '%s.%s (%s)' is annotated with '%s' but the method does not match the required signature (wrong return type, expected [%s] and found [%s])",
                                m.getDeclaringClass().getName(), m.getName(), StringUtils.join(StringUtils.join(m.getParameterTypes()), ", "), annotationClass.getName(), returnType, m.getReturnType()));
                        break;
                    }
                }
            }
            if (sb.length() > 0) {
                throw new InitializationException(sb.toString());
            }
        }

        LOG.trace(" ");
    }

    private boolean matchPrototype(Prototype prototype, Method m) {

        Class returnType = prototype.getExpectedReturnType();

        if (!isParametersMatching(prototype, m)) {
            return false;
        }

        //noinspection unchecked
        if (returnType != null && !returnType.isAssignableFrom(m.getReturnType())) {
            return false;
        }

        LOG.trace(String.format("- - - Found '%s.%s'", m.getDeclaringClass().getName(), m.getName()));

        return true;
    }

    private boolean isParametersMatching(Prototype prototype, Method m) {
        Class[] expectedParameterTypes = prototype.getExpectedParameterTypes();
        Class[] pc = m.getParameterTypes();
        if (pc.length != expectedParameterTypes.length) {
            return false;
        }
        for (int i = 0; i < pc.length; i++) {
            //noinspection unchecked
            if (!expectedParameterTypes[i].isAssignableFrom(pc[i])) {
                return false;
            }
        }
        return true;
    }

    private void assertModuleDependencies(CachedModule module) {
        try {
            if (module.getDependenciesMethod() == null) {
                LOG.debug(String.format("Module '%s' has no dependencies", module.getName()));
                return;
            }

            @SuppressWarnings("unchecked") Collection<Dependency> dependencies = (Collection<Dependency>) module.getDependenciesMethod().invoke(module.getBean());

            for (Dependency dependency : dependencies) {
                CachedModule provider = moduleRepository.getModule(dependency.getName());
                if (provider == null) {
                    throw new InitializationException(String.format("Module '%s' requires '%s' but it is not installed", module.getName(), dependency));
                }
                LOG.debug(String.format("Module '%s' requires module '%s' ", module.getName(), dependency));
                if (dependency.isMinimum()) {
                    if (dependency.getMajor() < provider.getModuleVersion().major()) {
                        if (dependency.getMinor() < provider.getModuleVersion().minor()) {
                            if (dependency.getPatch() < provider.getModuleVersion().patch()) {
                                throw new InitializationException(String.format("Module '%s' requires '%s' but the installed version is %s", module.getName(), dependency, provider.version()));
                            }
                        }
                    }
                } else {
                    if (dependency.getMajor() > provider.getModuleVersion().major()) {
                        if (dependency.getMinor() > provider.getModuleVersion().minor()) {
                            if (dependency.getPatch() > provider.getModuleVersion().patch()) {
                                throw new InitializationException(String.format("Module '%s' requires '%s' but the installed version is %s", module.getName(), dependency, provider.version()));
                            }
                        }
                    }
                }
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InitializationException("Unable to get annotations from module", e);
        }
    }

    private List<Object> getSortedModuleBeans() {

        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Module.class);

        final List<Object> sortedModulesClasses = Lists.newArrayList();
        sortedModulesClasses.addAll(beans.values());
        Collections.sort(sortedModulesClasses, new Comparator<Object>() {
            @Override
            public int compare(Object c1, Object c2) {
                Module m1 = c1.getClass().getAnnotation(Module.class);
                Module m2 = c2.getClass().getAnnotation(Module.class);
                return new Integer(m1.order()).compareTo(m2.order());
            }
        });
        return sortedModulesClasses;
    }

}
