package org.trinity.foundation.api.render.binding.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.trinity.foundation.api.render.binding.view.PropertySlot;

/***************************************
 * Marks an object as the view of a {@link PaintableSurfaceNode}. The view
 * should be enclosed by the {@code PaintableSurfaceNode} that has the
 * properties that should be handled. In order for the marked view to handle
 * changes of properties, methods of the view must be marked with
 * {@link PropertySlot}.
 * 
 *************************************** 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface View {

}