package org.trinity.foundation.api.render.binding.refactor.view;

public interface PropertyAdapter<R, T> {
	R adapt(T property);
}
