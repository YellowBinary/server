package org.yellowbinary.cms.server.core.annotation;

import com.google.common.collect.Sets;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;

public class ReflectionInvoker {

    public static <T> T execute(CachedAnnotation cachedAnnotation, Object... args) {

        try {
            Class[] parameterTypes = cachedAnnotation.getMethod().getParameterTypes();
            Set<Object> validatedArgs = getValidatedArgs(parameterTypes, args);

            assertAllArgsFound(parameterTypes, validatedArgs, args);

            //noinspection unchecked
            return (T) cachedAnnotation.getMethod().invoke(cachedAnnotation.getBean(), validatedArgs.toArray());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to invoke method [" + cachedAnnotation.getMethod().toString() + "]", e.getCause());
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Method [" + cachedAnnotation.getMethod().toString() + "] threw an exception", e.getTargetException());
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
            throw new RuntimeException("Unable to match up parameter types with arguments, " +
                    "parameter types = "+ Arrays.toString(parameterTypes)+", arguments = "+Arrays.toString(getArgTypes(args)));
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
            return (T) f.get(o);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException("Unable to get field value for ["+ name +"] of class [" + o.getClass().toString() + "]", e.getCause());
        }
    }
}
