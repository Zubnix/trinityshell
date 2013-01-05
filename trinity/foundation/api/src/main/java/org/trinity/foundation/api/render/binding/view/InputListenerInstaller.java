package org.trinity.foundation.api.render.binding.view;

import org.trinity.foundation.api.display.input.Input;

public interface InputListenerInstaller {

	void installInputListener(	Class<? extends Input> inputType,
								Object view,
								Object inputEventTarget,
								String inputSlotName);

	void removeInputListener(	Class<? extends Input> inputType,
								Object view,
								Object inputEventTarget,
								String inputSlotName);
}
