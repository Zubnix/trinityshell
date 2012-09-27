package org.trinity.shellplugin.widget.api.binding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ViewAttribute {

	/***************************************
	 * The id of the {@link ViewAttribute} in case multiple
	 * {@link ViewAttribute}s with the same name are present.
	 * 
	 * @return
	 *************************************** 
	 */
	String id() default "";

	/****************************************
	 * The name of the {@link ViewAttribute}.
	 * 
	 * @return
	 *************************************** 
	 */
	String value();

}
