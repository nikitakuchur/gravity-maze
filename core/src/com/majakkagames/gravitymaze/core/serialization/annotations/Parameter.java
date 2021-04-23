package com.majakkagames.gravitymaze.core.serialization.annotations;

import com.majakkagames.gravitymaze.core.game.GameObject;
import com.majakkagames.gravitymaze.core.serialization.Parameters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The parameter annotation. You can mark some field or getter/setter method of a class that implements
 * Parameterizable interface and they will serialize/deserialize automatically. You can use it instead of overriding
 * {@link GameObject#getParameters()} and {@link GameObject#setParameters(Parameters)}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Parameter {
    String name() default "";
}
