package org.hyperdrive.api.widget;

import org.hydrogen.api.display.input.MouseInput;

public interface Button extends Widget {

	public interface View extends Widget.View {
		PaintInstruction<Void> mouseButtonPressed(final MouseInput input);

		PaintInstruction<Void> mouseButtonReleased(final MouseInput input);
	}

	@Override
	public View getView();
}
