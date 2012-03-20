package org.hyperdrive.api.widget;

import org.hyperdrive.api.core.RenderArea;

public interface CloseButton extends Button, RectangleManipulator {

	public interface View extends Button.View, RectangleManipulator.View {
		PaintInstruction<Void> clientWindowClosed(final RenderArea targetWindow);
	}

	@Override
	public View getView();

	void closeClientWindow();
}
