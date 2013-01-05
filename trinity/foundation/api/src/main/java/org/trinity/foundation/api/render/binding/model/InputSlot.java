package org.trinity.foundation.api.render.binding.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.display.input.Momentum;

/***************************************
 * Mark a method as a handler of {@link Input}.
 * 
 *************************************** 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InputSlot {

	Momentum[] momentum() default { Momentum.STARTED, Momentum.STOPPED };

	String[] modifier() default {};
}
