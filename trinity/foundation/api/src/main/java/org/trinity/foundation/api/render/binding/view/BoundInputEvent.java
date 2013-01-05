package org.trinity.foundation.api.render.binding.view;

import org.trinity.foundation.api.display.input.Input;

public interface BoundInputEvent {

	Input getInput();

	String getInputSlotName();
}
