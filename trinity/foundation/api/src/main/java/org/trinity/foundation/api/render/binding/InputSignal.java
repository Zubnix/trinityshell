package org.trinity.foundation.api.render.binding;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.trinity.foundation.api.display.input.Input;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface InputSignal {
	Class<? extends Input> inputType();

	String inputSlotName();
}
