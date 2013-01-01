package org.trinity.foundation.api.render.binding.refactor.view;

public interface InputListenerInstaller {

	public void installKeyInputListener(Object view,
										Object inputEventTarget,
										String inputSlotName);

	public void installButtonInputListener(	Object view,
											Object inputEventTarget,
											String inputSlotName);
}
