package org.trinity.foundation.api.render.binding.view;

public class DefaultPropertyAdapter implements PropertyAdapter<Object> {

	@Override
	public Object adapt(final Object property) {
		return property;
	}

}
