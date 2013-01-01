package org.trinity.foundation.api.render.binding.refactor.view;

public class DefaultPropertyAdapter implements PropertyAdapter<Object, Object> {

	@Override
	public Object adapt(final Object property) {
		return property;
	}

}
