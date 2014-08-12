package org.yellowbinary.cms.server.core.event;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yellowbinary.cms.server.core.*;
import org.yellowbinary.cms.server.core.annotation.CachedAnnotation;
import org.yellowbinary.cms.server.core.annotation.ReflectionInvoker;
import org.yellowbinary.cms.server.core.model.RootNode;
import org.yellowbinary.cms.server.core.model.content.Text;
import org.yellowbinary.cms.server.core.stereotypes.OnLoad;

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


    /* ************************** */
    /* Root Node Event Generators */
    /* ************************** */
    public void triggerBeforeInterceptor(RootNode rootNode, String node, String type, Map<Object, Object> args) {
        List<CachedAnnotation> interceptors = findInterceptorForType(node, !StringUtils.isBlank(type) ? type : rootNode.getClass().getName(), false);
        if (interceptors != null && !interceptors.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptors) {
                ReflectionInvoker.execute(cachedAnnotation, args);
            }
        }
    }

    /* ************************** */
    /* Node Event Generators      */
    /* ************************** */

    public void triggerBeforeInterceptor(Node node, String type, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptors = findInterceptorForType(type, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), false);
        if (interceptors != null && !interceptors.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptors) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, args);
            }
        }
    }

    public void triggerAfterInterceptor(Node node, String type, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptorList = findInterceptorForType(type, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (interceptorList != null && !interceptorList.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, args);
            }
        }
    }

/*
    public void triggerBeforeInterceptor(Node node, String type, String withType, Navigation navigation, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptors = findInterceptorForType(type, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), false);
        if (interceptors != null && !interceptors.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptors) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, navigation, args);
            }
        }
    }

    public void triggerBeforeInterceptor(Node node, String type, String withType, Form form, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptors = findInterceptorForType(type, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), false);
        if (interceptors != null && !interceptors.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptors) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, form, args);
            }
        }
    }

    public void triggerBeforeInterceptor(Node node, String type, String withType, Element element, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptors = findInterceptorForType(type, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), false);
        if (interceptors != null && !interceptors.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptors) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, element, args);
            }
        }
    }

    public void triggerBeforeInterceptor(Node node, String type, String withType, Text text, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptors = findInterceptorForType(type, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), false);
        if (interceptors != null && !interceptors.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptors) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, text, args);
            }
        }
    }
*/

/*
    public void triggerAfterInterceptor(Node node, String onLoadType, String withType, Navigation navigation, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptorList = findInterceptorForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (interceptorList != null && !interceptorList.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, navigation, args);
            }
        }
    }

    public void triggerAfterInterceptor(Node node, String onLoadType, String withType, Form form, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptorList = findInterceptorForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (interceptorList != null && !interceptorList.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, form, args);
            }
        }
    }

    public void triggerAfterInterceptor(Node node, String onLoadType, String withType, Element element, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptorList = findInterceptorForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (interceptorList != null && !interceptorList.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, element, args);
            }
        }
    }

    public void triggerAfterInterceptor(Node node, String onLoadType, String withType, Form form, Element element, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptorList = findInterceptorForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (interceptorList != null && !interceptorList.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, form, element, args);
            }
        }
    }

    public void triggerAfterInterceptor(Node node, String onLoadType, String withType, Navigation navigation, NavigationElement element, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptorList = findInterceptorForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (interceptorList != null && !interceptorList.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, navigation, element, args);
            }
        }
    }
*/

    public void triggerAfterInterceptor(Node node, String onLoadType, String withType, Text text, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<CachedAnnotation> interceptorList = findInterceptorForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (interceptorList != null && !interceptorList.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                ReflectionInvoker.execute(cachedAnnotation, node, withType, text, args);
            }
        }
    }

    private List<CachedAnnotation> findInterceptorForType(final String onLoadType, final String withType, final boolean after) {
        List<CachedAnnotation> interceptors = Lists.newArrayList(interceptorRepository.getInterceptors(OnLoad.class, new InterceptorRepository.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cachedAnnotation) {
                OnLoad annotation = ((OnLoad) cachedAnnotation.getAnnotation());
                return annotation.base().equals(onLoadType) && annotation.after() == after &&
                        (StringUtils.isBlank(annotation.with()) || annotation.with().equals(withType));
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
