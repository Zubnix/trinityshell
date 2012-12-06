package org.trinity.shellplugin.widget.api.binding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.trinity.foundation.render.api.PaintableSurfaceNode;

/***************************************
 * Marks an object as the view of a {@link PaintableSurfaceNode}. The view
 * should be encapsulated by the {@code PaintableSurfaceNode} that has the
 * properties that you wish to handle. In order for the view to handle changes
 * of properties, you must annotated the desired view methods with
 * {@link ViewPropertySlot}.
 * 
 *************************************** 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface ViewReference {

}