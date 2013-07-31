package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.render.binding.view.EventSignal;
import org.trinity.foundation.api.render.binding.view.InputSignals;
import org.trinity.foundation.api.render.binding.view.PropertySlot;
import org.trinity.foundation.api.render.binding.view.PropertySlots;

@InputSignals({ @EventSignal(name = "onClick",filter=DummyEventSignalFilter.class) })
@PropertySlots(@PropertySlot(propertyName = "booleanProperty", methodName = "handleBooleanProperty", argumentTypes = boolean.class))
public class CollectionElementView {
	public void handleBooleanProperty(final boolean booleanProperty) {

	}

	public void handleStringProperty(final String string) {

	}
}
