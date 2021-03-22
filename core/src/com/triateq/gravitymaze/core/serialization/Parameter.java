package com.triateq.gravitymaze.core.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The parameter annotation. You can mark some field or getter/setter method of a class that implements
 * Parameterizable interface and they will serialize/deserialize automatically. You can use it instead of overriding
 * {@link Parameterizable#getParameters()} and {@link Parameterizable#setParameters(Parameters)}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Parameter {
    String name() default "";
}
