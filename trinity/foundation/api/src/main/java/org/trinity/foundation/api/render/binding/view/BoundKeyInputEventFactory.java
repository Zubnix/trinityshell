package org.trinity.foundation.api.render.binding.view;

import org.trinity.foundation.api.display.input.KeyboardInput;

public interface BoundKeyInputEventFactory {
	BoundKeyInputEvent createBoundKeyInputEvent(final Object inputTarget,
												final KeyboardInput input,
												final String inputSlotName);
}
