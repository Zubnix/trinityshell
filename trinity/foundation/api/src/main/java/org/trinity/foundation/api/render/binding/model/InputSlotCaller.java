package org.trinity.foundation.api.render.binding.model;

import org.trinity.foundation.api.render.binding.view.BoundInputEvent;

public interface InputSlotCaller {

	void notifyDataContextInputSlot(Object model,
									BoundInputEvent boundInputEvent);
}