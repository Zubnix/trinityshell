package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.render.binding.refactor.view.PropertyAdapter;

public class BooleanToStringAdapter implements PropertyAdapter<String, Boolean> {

	@Override
	public String adapt(final Boolean property) {
		return String.valueOf(property);
	}
}