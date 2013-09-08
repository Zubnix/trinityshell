package org.trinity.shellplugin.wm.api.viewreferencekey;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@BindingAnnotation
@Target({ TYPE, FIELD, PARAMETER, METHOD })
@Retention(RUNTIME)
public @interface DesktopViewReference {
}
