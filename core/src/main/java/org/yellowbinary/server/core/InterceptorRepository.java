package org.yellowbinary.server.core;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Repository;
import org.yellowbinary.server.core.annotation.CachedAnnotation;
import org.yellowbinary.server.core.annotation.CachedModule;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Repository
public class InterceptorRepository {

    public Map<Class<? extends Annotation>, Set<CachedAnnotation>> interceptors = Maps.newHashMap();

    public void add(CachedModule module, Annotation annotation, Method method, Object bean) {
        if (!interceptors.containsKey(annotation.annotationType())) {
            interceptors.put(annotation.annotationType(), Sets.<CachedAnnotation>newHashSet());
        }
        Set<CachedAnnotation> annotationTypes = interceptors.get(annotation.annotationType());
        annotationTypes.add(new CachedAnnotation(module, annotation, method, bean));
    }

    public Set<CachedAnnotation> getInterceptors(Class<? extends Annotation> annotationType) {
        return getInterceptors(annotationType, null);
    }

    public Set<CachedAnnotation> getInterceptors(Class<? extends Annotation> annotationType, InterceptorSelector interceptorSelector) {
        if (interceptors.containsKey(annotationType)) {
            Set<CachedAnnotation> interceptorList = interceptors.get(annotationType);
            if (interceptorSelector == null) {
                return interceptorList;
            }
            Set<CachedAnnotation> matchingCachedAnnotations = Sets.newHashSet();
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                if (interceptorSelector.isCorrectInterceptor(cachedAnnotation)) {
                    matchingCachedAnnotations.add(cachedAnnotation);
                }
            }
            return matchingCachedAnnotations;
        } else {
            return Collections.emptySet();
        }
    }

    public void invalidate() {
        interceptors.clear();
    }

    public Map<Class<? extends Annotation>, Set<CachedAnnotation>> getInterceptorMap() {
        return Collections.unmodifiableMap(interceptors);
    }

    public static interface InterceptorSelector {
        boolean isCorrectInterceptor(CachedAnnotation cachedAnnotation);
    }

}
