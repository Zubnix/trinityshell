package org.trinity.foundation.api.render.binding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***************************************
 * Declare a getter method as a view property. Each view property must have a
 * unique name {@link #value()}. If no name is given it will be derived from the
 * getter method name, that is the name of the getter method without 'get' and
 * will start with a lower case.
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
