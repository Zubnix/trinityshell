package org.trinity.foundation.api.render.binding.refactor.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.trinity.foundation.api.render.binding.refactor.model.ViewProperty;

/***************************************
 * Marks a view method as the handler method for a changed property. A property
 * is linked to a slot by matching it's {@link ViewProperty#value()} to one the
 * {@link #value()}s of the slot.
 * 
 *************************************** 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface PropertySlot {
	String propertyName();

	String methodName();

	Class<?>[] argumentTypes();

	// TODO perhaps we want to use a provider to instantiate a certain type of
	// adapters?
	Class<? extends PropertyAdapter<?, ?>> adapter() default DefaultPropertyAdapter.class;
}
