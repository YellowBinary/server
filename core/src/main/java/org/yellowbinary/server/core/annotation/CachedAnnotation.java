package org.yellowbinary.server.core.annotation;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class CachedAnnotation implements Comparable<CachedAnnotation> {

    private final CachedModule module;
    private final Annotation annotation;
    private final Method method;
    private final Object bean;

    public CachedAnnotation(CachedModule module, Annotation annotation, Method method, Object bean) {
        this.module = module;
        this.annotation = annotation;
        this.method = method;
        this.bean = bean;
    }

    public CachedModule getModule() {
        return module;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public Method getMethod() {
        return method;
    }

    public Object getBean() {
        return bean;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CachedAnnotation that = (CachedAnnotation) o;

        return !(annotation != null ? !annotation.equals(that.annotation) : that.annotation != null)
                && !(method != null ? !method.equals(that.method) : that.method != null)
                && !(bean != null ? !bean.equals(that.bean) : that.bean != null);
    }

    @Override
    public int hashCode() {
        int result = annotation != null ? annotation.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (bean != null ? bean.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(CachedAnnotation cachedAnnotation) {
        return new Integer(hashCode()).compareTo(cachedAnnotation.hashCode());
    }

}
