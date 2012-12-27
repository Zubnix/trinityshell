package org.trinity.foundation.api.render.binding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.trinity.foundation.api.render.binding.InputSignal;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface InputEmitter {
	InputSignal[] value();
}
