package org.yellowbinary.server.core.annotation;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.yellowbinary.server.core.InitializationException;
import org.yellowbinary.server.core.InterceptorRepository;
import org.yellowbinary.server.core.ModuleRepository;
import org.yellowbinary.server.core.stereotypes.Interceptor;
import org.yellowbinary.server.core.stereotypes.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

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
            LOG.debug("Modules registered: " + moduleRepository.getAll().size() + "\n" + sb.toString());

            sb = new StringBuilder();
            int count = 0;
            Map<Class<? extends Annotation>, Set<CachedAnnotation>> interceptorMap = interceptorRepository.getInterceptorMap();
            for (Class<? extends Annotation> a : interceptorMap.keySet()) {
                Set<CachedAnnotation> interceptors = interceptorMap.get(a);
                sb.append(" - ").append(a.getName()).append(" ").append(interceptors.size()).append("\n");
                count += interceptors.size();
            }
            LOG.debug("Interceptors registered: " + count + "\n" + sb.toString());

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
            LOG.trace("- Processing module '" + module.getName() + "'");
            LOG.trace("------------------------------------------------");

            Map<String, Object> interceptors = applicationContext.getBeansWithAnnotation(Interceptor.class);

            try {
                if (module.getAnnotationsMethod() != null) {
                    //noinspection unchecked
                    List<Prototype> prototypes = (List<Prototype>) module.getAnnotationsMethod().invoke(module.getBean());
                    for (Prototype prototype : prototypes) {
                        if (!annotationPrototypes.containsKey(prototype.annotation)) {
                            annotationPrototypes.put(prototype.annotation, Lists.<Prototype>newArrayList());
                        }
                        List<Prototype> list = annotationPrototypes.get(prototype.annotation);
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

        LOG.trace("- - Processing [" + annotationClass.getSimpleName() + "]");

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
                    Class[] parameterTypes = prototype.expectedParameterTypes;
                    Class[] pc = m.getParameterTypes();
                    if (pc.length != parameterTypes.length) {
                        sb.append("Method '").append(m.getDeclaringClass()).append(".").append(m.getName()).append("(").append(StringUtils.join(StringUtils.join(m.getParameterTypes()))).append(")' is annotated with '").append(annotationClass.getName()).append("' but the method does not match the required signature (different amount of parameters)");
                        break;
                    }
                    for (int i = 0; i < pc.length; i++) {
                        //noinspection unchecked
                        if (!parameterTypes[i].isAssignableFrom(pc[i])) {
                            sb.append("Method '").append(m.getDeclaringClass().getName()).append(".").append(m.getName()).append("(").append(StringUtils.join(StringUtils.join(m.getParameterTypes()))).append(")' is annotated with '").append(annotationClass.getName()).append("' but the method does not match the required signature (parameter '").append(parameterTypes[i].getName()).append("' has the wrong type)");
                            break;
                        }
                    }
                    // Parameters match so check return type
                    Class returnType = prototype.expectedReturnType;
                    //noinspection unchecked
                    if (returnType != null && !returnType.isAssignableFrom(m.getReturnType())) {
                        sb.append("Method '").append(m.getDeclaringClass().getName()).append(".").append(m.getName()).append("(").append(StringUtils.join(StringUtils.join(m.getParameterTypes()))).append(")' is annotated with '").append(annotationClass.getName()).append("' but the method does not match the required signature (wrong return type, expected [").append(returnType).append("] and found [").append(m.getReturnType()).append("])");
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

        Class returnType = prototype.expectedReturnType;

        if (!isParametersMatching(prototype, m)) {
            return false;
        }

        //noinspection unchecked
        if (returnType != null && !returnType.isAssignableFrom(m.getReturnType())) {
            return false;
        }

        LOG.trace("- - - Found '" + m.getDeclaringClass().getName() + "." + m.getName() + "'");

        return true;
    }

    private boolean isParametersMatching(Prototype prototype, Method m) {
        Class[] expectedParameterTypes = prototype.expectedParameterTypes;
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
                LOG.debug("Module '" + module.getName() + "' has no dependencies");
                return;
            }

            @SuppressWarnings("unchecked") Collection<Dependency> dependencies = (Collection<Dependency>) module.getDependenciesMethod().invoke(module);

            for (Dependency dependency : dependencies) {
                CachedModule provider = moduleRepository.getModule(dependency.name);
                if (provider == null) {
                    throw new InitializationException("Module '" + module.getName() + "' requires '" + dependency + "' but it is not installed");
                }
                LOG.debug("Module '" + module.getName() + "' requires module '" + dependency + "' ");
                if (dependency.minimum) {
                    if (dependency.major < provider.getModuleVersion().major()) {
                        if (dependency.minor < provider.getModuleVersion().minor()) {
                            if (dependency.patch < provider.getModuleVersion().patch()) {
                                throw new InitializationException("Module '" + module.getName() + "' requires '" + dependency + "' but the installed version is " + provider.version());
                            }
                        }
                    }
                } else {
                    if (dependency.major > provider.getModuleVersion().major()) {
                        if (dependency.minor > provider.getModuleVersion().minor()) {
                            if (dependency.patch > provider.getModuleVersion().patch()) {
                                throw new InitializationException("Module '" + module.getName() + "' requires '" + dependency + "' but the installed version is " + provider.version());
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

    public static class Prototype {
        private final Class<? extends Annotation> annotation;
        private final Class[] expectedParameterTypes;
        public final Class expectedReturnType;

        public Prototype(Class<? extends Annotation> annotation, Class expectedReturnType, Class... expectedParameterTypes) {
            this.annotation = annotation;
            this.expectedParameterTypes = expectedParameterTypes;
            this.expectedReturnType = expectedReturnType;
        }

        public Class<? extends Annotation> getAnnotation() {
            return annotation;
        }

        public Class[] getExpectedParameterTypes() {
            return expectedParameterTypes;
        }

        public Class getExpectedReturnType() {
            return expectedReturnType;
        }
    }

    public static class Dependency {

        private final String name;
        private final boolean minimum;
        private final int major;
        private final int minor;
        private final int patch;

        public Dependency(String name, int major, int minor) {
            this(name, true, major, minor);
        }

        public Dependency(String name, boolean minimum, int major, int minor) {
            this(name, minimum, major, minor, 0);
        }

        public Dependency(String name, boolean minimum, int major, int minor, int patch) {
            this.name = name;
            this.minimum = minimum;
            this.major = major;
            this.minor = minor;
            this.patch = patch;
        }

        public String getName() {
            return name;
        }

        public boolean isMinimum() {
            return minimum;
        }

        public int getMajor() {
            return major;
        }

        public int getMinor() {
            return minor;
        }

        public int getPatch() {
            return patch;
        }

        public String toString() {
            return "Module " + name + " (" + version() + ")";
        }

        public String version() {
            return (minimum ? ">=" : "<") + major + (minor != -1 ? "." + minor : "") + (patch != -1 ? "." + patch : "");
        }
    }
}
