package org.trinity.foundation.api.render.binding.view.delegate;

import org.trinity.foundation.api.display.input.Input;

public interface InputListenerInstallerDelegate {

	void installInputListener(	Class<? extends Input> inputType,
								Object view,
								Object inputEventTarget,
								String inputSlotName);

	void removeInputListener(	Class<? extends Input> inputType,
								Object view,
								Object inputEventTarget,
								String inputSlotName);
}
