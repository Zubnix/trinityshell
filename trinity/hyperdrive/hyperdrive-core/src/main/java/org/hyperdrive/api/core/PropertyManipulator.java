package org.hyperdrive.api.core;

import org.hydrogen.api.display.PropertyInstance;

public interface PropertyManipulator {
	RenderArea getRenderArea();

	<T extends PropertyInstance> T getPropertyValue(final String propertyName);

	<T extends PropertyInstance> void setPropertyValue(
			final String propertyName, final T propertyInstance);
}
