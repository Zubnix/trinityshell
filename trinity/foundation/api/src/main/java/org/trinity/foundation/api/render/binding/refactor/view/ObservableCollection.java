package org.trinity.foundation.api.render.binding.refactor.view;

public @interface ObservableCollection {

	// TODO somehow pass a Guice Provider to instantiate views?
	Class<?> value();
}