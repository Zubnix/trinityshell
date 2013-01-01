package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.render.binding.refactor.view.InputSignal;

public @interface ObservedCollection {

	String name();

	Class<?> view();

	InputSignal[] inputSignal() default {};
}
