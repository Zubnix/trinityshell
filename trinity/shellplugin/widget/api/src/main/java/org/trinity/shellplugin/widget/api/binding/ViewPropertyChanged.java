package org.trinity.shellplugin.widget.api.binding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/****************************************
 * Marks a method as a manipulator of a property. After the execution of a
 * marked method, the {@link ViewPropertySlot}s with a matching
 * {@link ViewProperty#value()} will be invoked. The most straightforward
 * implementation is thus to place this annotation on setter methods.
 * <p>
 * If more precise control is required, see
 * {@link BindingDiscovery#notifyViewPropertySlot(Class, Object, String...)}.
 * 
 *************************************** 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface ViewPropertyChanged {
	/****************************************
	 * The name(s) of the {@link ViewProperty}(s) that will be updated.
	 * 
	 * @return
	 *************************************** 
	 */
	String[] value();
}
