package org.trinity.foundation.api.render.binding.view;

import org.trinity.foundation.api.display.input.PointerInput;

public interface BoundButtonInputEventFactory {
	BoundButtonInputEvent creatBoundButtonInputEvent(	final Object inputTarget,
														final PointerInput pointerInput,
														final String inputSlotName);
}