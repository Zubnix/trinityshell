package org.hyperdrive.api.widget;

import org.hyperdrive.api.core.RenderArea;

public interface MaximizeButton extends Button, RectangleManipulator {

	public interface View extends Button.View, RectangleManipulator.View {
		PaintInstruction<Void> clientWindowMaximized(
				final RenderArea targetWindow);

		PaintInstruction<Void> clientWindowRestored(
				final RenderArea targetWindow);
	}

	@Override
	public View getView();

	void maximizeClientWindow();

	void restoreClientWindow();
}
