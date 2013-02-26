package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.render.binding.view.PropertyAdapter;

public class BooleanToStringAdapter implements PropertyAdapter<Boolean> {

	@Override
	public String adapt(final Boolean property) {
		return String.valueOf(property);
	}
}