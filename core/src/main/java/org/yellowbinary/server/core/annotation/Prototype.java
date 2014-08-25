package org.yellowbinary.server.core.annotation;

import java.lang.annotation.Annotation;

public class Prototype {

    private final Class<? extends Annotation> annotation;
    private final Class[] expectedParameterTypes;
    private final Class expectedReturnType;

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
