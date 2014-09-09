package org.yellowbinary.server.core.event;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yellowbinary.server.core.*;
import org.yellowbinary.server.core.annotation.CachedAnnotation;
import org.yellowbinary.server.core.annotation.ReflectionInvoker;
import org.yellowbinary.server.core.stereotypes.OnLoad;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Helper to trigger \@OnLoad interceptors. Should not be used directly, use NodeHelper instead.
 *
 * @see OnLoad
 */
@Component
public class OnLoadEventGenerator {

    @Autowired
    private InterceptorRepository interceptorRepository;

    public void triggerBeforeInterceptor(Object o, String type, String withType, Map<String, Object> args) throws ModuleException, InterceptorException {
        List<CachedAnnotation> interceptors = findInterceptorForType(type, !StringUtils.isBlank(withType) ? withType : o.getClass().getName(), false);
        if (interceptors != null && !interceptors.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptors) {
                ReflectionInvoker.execute(cachedAnnotation, o, withType, args);
            }
        }
    }

    public void triggerAfterInterceptor(Object o, String base, String with, Map<String, Object> args) throws ModuleException, InterceptorException {
        List<CachedAnnotation> interceptorList = findInterceptorForType(base, !StringUtils.isBlank(with) ? with : o.getClass().getName(), true);
        if (interceptorList != null && !interceptorList.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                ReflectionInvoker.execute(cachedAnnotation, o, with, args);
            }
        }
    }

    private List<CachedAnnotation> findInterceptorForType(final String onLoadType, final String with, final boolean after) {
        List<CachedAnnotation> interceptors = Lists.newArrayList(interceptorRepository.getInterceptors(OnLoad.class, new InterceptorRepository.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cachedAnnotation) {
                OnLoad annotation = ((OnLoad) cachedAnnotation.getAnnotation());
                return annotation.base().equals(onLoadType) && annotation.after() == after &&
                        (StringUtils.isBlank(annotation.with()) || annotation.with().equals(with));
            }
        }));
        Collections.sort(interceptors, new Comparator<CachedAnnotation>() {
            @Override
            public int compare(CachedAnnotation o1, CachedAnnotation o2) {
                int weight1 = ((OnLoad) o1.getAnnotation()).weight();
                int weight2 = ((OnLoad) o2.getAnnotation()).weight();
                return new Integer(weight1).compareTo(weight2);
            }
        });
        return interceptors;
    }
}
