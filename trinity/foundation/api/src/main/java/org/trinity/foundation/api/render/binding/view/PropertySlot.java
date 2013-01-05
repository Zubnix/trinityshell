package org.trinity.foundation.api.render.binding.view;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***************************************
 * Marks a view method as the handler method for a changed property. A property
 * is linked to a slot by matching it's {@link ViewProperty#value()} to one the
 * {@link #value()}s of the slot.
 * 
 *************************************** 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface PropertySlot {
	String propertyName();

	String methodName();

	Class<?>[] argumentTypes();

	// TODO perhaps we want to use a provider to instantiate a certain type of
	// adapters?
	Class<? extends PropertyAdapter<?>> adapter() default DefaultPropertyAdapter.class;
}
