package org.trinity.shellplugin.widget.api.binding;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.trinity.foundation.input.api.Input;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface InputSignal {
	Class<? extends Input> inputType();

	String inputSlotName();
}
