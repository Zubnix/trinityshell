package org.trinity.shellplugin.widget.api.binding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***************************************
 * Declare a field or method as a view property. Each view property must have a
 * unique name {@link #value()}.
 * 
 *************************************** 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface ViewProperty {

	/****************************************
	 * The unique name of the property.
	 * 
	 * @return a {@code String}
	 *************************************** 
	 */
	String value() default "";

}
