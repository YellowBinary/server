package org.yellowbinary.server.core.annotation;

import org.yellowbinary.server.core.stereotypes.Module;

import java.lang.reflect.Method;

public class CachedModule implements Comparable<CachedModule> {

    private final String name;
    private final Object bean;

    private final Module annotation;
    private final Module.Version moduleVersion;

    private final Method initMethod;
    private final Method annotationsMethod;
    private final Method dependenciesMethod;

    public CachedModule(String name, Object bean, Module annotation, Module.Version moduleVersion, Method initMethod,
                        Method annotationsMethod, Method dependencies) {
        this.name = name;
        this.annotation = annotation;
        this.bean = bean;
        this.moduleVersion = moduleVersion;
        this.initMethod = initMethod;
        this.annotationsMethod = annotationsMethod;
        this.dependenciesMethod = dependencies;
    }

    public String getName() {
        return name;
    }

    public Object getBean() {
        return bean;
    }

    public Module getAnnotation() {
        return annotation;
    }

    public Module.Version getModuleVersion() {
        return moduleVersion;
    }

    public Method getInitMethod() {
        return initMethod;
    }

    public Method getAnnotationsMethod() {
        return annotationsMethod;
    }

    public Method getDependenciesMethod() {
        return dependenciesMethod;
    }

    @Override
    public int compareTo(CachedModule that) {
        return new Integer(annotation.order()).compareTo(that.annotation.order());
    }

    public String toString() {
        return "Module '"+name+"' ("+version()+")";
    }

    public String version() {
        return moduleVersion != null ? moduleVersion.major()+"."+moduleVersion.minor()+"."+moduleVersion.patch() : "";
    }

}
