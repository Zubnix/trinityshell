package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.display.input.PointerInput;
import org.trinity.foundation.api.render.binding.refactor.view.InputSignal;
import org.trinity.foundation.api.render.binding.refactor.view.InputSignals;
import org.trinity.foundation.api.render.binding.refactor.view.PropertySlot;
import org.trinity.foundation.api.render.binding.refactor.view.SubModel;

@SubModel("subModel")
@InputSignals({ @InputSignal(name = "onClick", inputType = PointerInput.class) })
@PropertySlot(propertyName = "booleanProperty", methodName = "handleBooleanProperty", argumentTypes = boolean.class)
public class SubView {

	public void handleBooleanProperty(final boolean booleanProperty) {

	}

	public void handleStringProperty(final String string) {

	}
}
