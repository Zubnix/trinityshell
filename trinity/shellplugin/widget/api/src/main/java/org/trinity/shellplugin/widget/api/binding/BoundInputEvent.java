package org.trinity.shellplugin.widget.api.binding;

import org.trinity.foundation.input.api.Input;

public interface BoundInputEvent {

	Input getInput();

	String getInputSlotName();
}
