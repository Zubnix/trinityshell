package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.display.input.PointerInput;
import org.trinity.foundation.api.render.binding.view.DataContext;
import org.trinity.foundation.api.render.binding.view.InputSignal;
import org.trinity.foundation.api.render.binding.view.InputSignals;
import org.trinity.foundation.api.render.binding.view.PropertySlot;
import org.trinity.foundation.api.render.binding.view.PropertySlots;

@DataContext("dummySubModel")
@InputSignals({ @InputSignal(name = "onClick", inputType = PointerInput.class) })
@PropertySlots(@PropertySlot(propertyName = "booleanProperty", methodName = "handleBooleanProperty", argumentTypes = boolean.class))
public class SubView {

	public void handleBooleanProperty(final boolean booleanProperty) {

	}

	public void handleStringProperty(final String string) {

	}
}
