package org.trinity.foundation.api.render.binding;

import java.util.concurrent.ExecutionException;

public interface Binder {

	void updateBinding(	Object changedModel,
						String propertyName) throws ExecutionException;

	void bind(	Object model,
				Object view) throws ExecutionException;

}
