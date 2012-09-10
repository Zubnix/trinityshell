package org.trinity.shellplugin.widget.api.mvvm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/****************************************
 * Immediately after the invocation of the annotated method, the {@link VisualReference}s
 * with the same given {@link #value()} will be updated to reflect their field's
 * state. The most straightforward implementation is thus to place this
 * annotation on setter methods.
 * <p>
 * Only declared fields are taken into consideration. Inherited fields will be
 * ignored.
 * <p>
 * Please note this is a workaround for Guice's lack of field level AOP
 * annotations. Ideally we'd place an annotation directly on the field itself.
 * 
 *************************************** 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface ViewSignal {
	/****************************************
	 * The id(s) of the visual(s) that will be updated.
	 * 
	 * @return
	 *************************************** 
	 */
	String[] value();
}
