package org.yellowbinary.server.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.yellowbinary.server.core.annotation.CachedModule;
import org.yellowbinary.server.core.stereotypes.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

@Repository
public class ModuleRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ModuleRepository.class);

    @Autowired
    private Configuration configuration;

    private Map<String, CachedModule> modules = Maps.newHashMap();

    public CachedModule add(Module module, Object bean) {
        if (modules.containsKey(module.name())) {
            throw new InitializationException("Duplicate Module name ["+module.name()+"]");
        }
        if (module.order() < 0) {
            throw new InitializationException("Order must be higher than 0");
        }

        //noinspection unchecked
        Module.Version moduleVersionAnnotation = bean.getClass().getAnnotation(Module.Version.class);

        Method initMethod = getSingleMethod(bean.getClass(), Module.Init.class);
        Method dependenciesMethod = getSingleMethod(bean.getClass(), Module.Dependencies.class);
        Method annotationsMethod = getSingleMethod(bean.getClass(), Module.Annotations.class);

        CachedModule value = new CachedModule(module.name(), bean, module, moduleVersionAnnotation,
                initMethod, annotationsMethod, dependenciesMethod);
        modules.put(module.name(), value);
        return value;
    }

    private Method getSingleMethod(Class c, final Class<? extends Annotation> annotation) {
        Set<Method> methods = Reflections.getAllMethods(c, ReflectionUtils.withAnnotation(annotation));
        if (methods.size() == 1) {
            return methods.iterator().next();
        }
        LOG.warn(String.format("More than one %s annotation in class %s, skipping", annotation.getClass().getSimpleName(), c.getSimpleName()));
        return null;
    }

    public CachedModule getModule(String name) {
        return modules.get(name);
    }

    public void invalidate() {
        modules.clear();
    }

    public List<CachedModule> getAll() {
        ArrayList<CachedModule> moduleList = Lists.newArrayList(modules.values());
        Collections.sort(moduleList);
        return moduleList;
    }

    public boolean isEnabled(CachedModule module) {
        return configuration.readValue(Boolean.class, "module." + module.getName() + ".enabled", true);
    }

    public void enable(CachedModule module) {
        configuration.updateValue("module."+ module.getName()+ ".enabled", true);
    }

    public void enable(String moduleName) throws ModuleException {
        if (!modules.containsKey(moduleName)) {
            LOG.error("Unknown module '"+moduleName+"'");
            throw new ModuleException(moduleName, ModuleException.Reason.NOT_INSTALLED, "Unknown module '"+moduleName+"'");
        }

        enable(modules.get(moduleName));
    }
}
