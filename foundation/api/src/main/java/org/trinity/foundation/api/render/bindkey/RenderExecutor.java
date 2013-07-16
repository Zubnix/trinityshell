package org.trinity.foundation.api.render.bindkey;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

/**
 * Used for the render ListenableExecutorService Singleton thread context. Not provided by api.
 */
@BindingAnnotation
@Target({ TYPE, FIELD, PARAMETER, METHOD })
@Retention(RUNTIME)
public @interface RenderExecutor {
}
