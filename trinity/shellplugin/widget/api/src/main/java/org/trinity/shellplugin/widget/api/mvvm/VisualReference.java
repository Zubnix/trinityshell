package org.trinity.shellplugin.widget.api.mvvm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface VisualReference {
	/****************************************
	 * The id of the visual.
	 * 
	 * @return
	 *************************************** 
	 */
	String value();
}