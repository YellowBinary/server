package org.yellowbinary.server.core.annotation;

import com.google.common.collect.Sets;
import org.springframework.security.access.AccessDeniedException;
import org.yellowbinary.server.core.InterceptorException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;

public class ReflectionInvoker {

    public static <T> T execute(CachedAnnotation cachedAnnotation, Object... args) throws InterceptorException {

        try {
            Class[] parameterTypes = cachedAnnotation.getMethod().getParameterTypes();
            Set<Object> validatedArgs = getValidatedArgs(parameterTypes, args);

            assertAllArgsFound(parameterTypes, validatedArgs, args);

            //noinspection unchecked
            return (T) cachedAnnotation.getMethod().invoke(cachedAnnotation.getBean(), validatedArgs.toArray());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("Unable to invoke method [%s]", cachedAnnotation.getMethod()), e.getCause());
        } catch (InvocationTargetException e) {
            // If this is a SecurityException we want to propagate that
            if (e.getTargetException() instanceof AccessDeniedException) {
                throw (AccessDeniedException)e.getTargetException();
            }
            throw new InterceptorException(String.format("Method [%s] threw an exception", cachedAnnotation.getMethod()), e.getTargetException());
        }

    }

    private static Set<Object> getValidatedArgs(Class[] parameterTypes, Object[] args) {
        Set<Object> validatedArgs = Sets.newLinkedHashSet();
        Set<Object> extraArgs = Sets.newHashSet();
        for (Class parameterType : parameterTypes) {
            for (int l = extraArgs.size() + validatedArgs.size(); l < args.length; l++) {
                //noinspection unchecked
                if (args[l] == null || parameterType.isAssignableFrom(args[l].getClass())) {
                    validatedArgs.add(args[l]);
                    break;
                } else {
                    extraArgs.add(args[l]);
                }
            }
        }
        return validatedArgs;
    }

    private static void assertAllArgsFound(Class[] parameterTypes, Set<Object> validatedArgs, Object[] args) {
        if (parameterTypes.length != validatedArgs.size()) {
            throw new RuntimeException(String.format("Unable to match up parameter types with arguments, parameter types %s, arguments %s", Arrays.toString(parameterTypes), Arrays.toString(getArgTypes(args))));
        }
    }

    private static Class[] getArgTypes(Object[] args) {
        Set<Class> types = Sets.newHashSet();
        for (Object o : args) {
            types.add(o.getClass());
        }
        return types.toArray(new Class[types.size()]);
    }

    public static <T> T getFieldValue(Object o, String name) {
        try
        {
            Field f = o.getClass().getField(name);
            //noinspection unchecked
            return (T) f.get(o);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException("Unable to get field value for ["+ name +"] of class [" + o.getClass().toString() + "]", e.getCause());
        }
    }
}
