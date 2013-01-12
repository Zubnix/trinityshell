package org.trinity.foundation.api.render.binding.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE })
public @interface ObservableCollection {

	String value();

	// TODO somehow pass a Guice Provider to instantiate views?
	Class<?> view();
}