package org.trinity.foundation.api.render.binding.view;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a view type or a field of a view as a bindable view. This annotation makes the object
 * discoverable for view binding if the referencer is being bound.  The current resolved
 * datacontext will be used to bind the marked view object.
 */
@Target({FIELD,
         TYPE})
@Retention(RUNTIME)
public @interface SubView {
}
