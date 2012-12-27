package org.trinity.foundation.api.render.binding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.trinity.foundation.api.render.binding.ViewProperty;

/***************************************
 * Marks a view method as the handler method for a changed property. A property
 * is linked to a slot by matching it's {@link ViewProperty#value()} to one the
 * {@link #value()}s of the slot.
 * 
 *************************************** 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface ViewPropertySlot {
	/***************************************
	 * The names of the properties that will be tracked by this slot.
	 * 
	 * @return an array of {@code String}s.
	 *************************************** 
	 */
	String[] value();
}
