package org.trinity.shell.api.bindingkey;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

/**
 * Used for the root ShellNodeParent Singleton. Not provided by api.
 */
@BindingAnnotation
@Target({ TYPE, FIELD, PARAMETER, METHOD })
@Retention(RUNTIME)
public @interface ShellRootNode {
}
