package org.trinity.shellplugin.widget.api.binding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.trinity.foundation.input.api.Input;
import org.trinity.foundation.input.api.Momentum;

/***************************************
 * Mark a method as a handler of {@link Input}.
 * 
 *************************************** 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InputSlot {

	String name() default "";

	Class<? extends Input> input() default Input.class;

	Momentum[] momentum() default { Momentum.STARTED, Momentum.STOPPED };

	String[] modifier() default {};
}
