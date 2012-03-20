package org.hyperdrive.api.widget;

import org.hyperdrive.api.core.RenderArea;

public interface DragButton extends Button, RectangleManipulator {

	public interface View extends Button.View, RectangleManipulator.View {
		PaintInstruction<Void> clientWindowStartDrag(
				final RenderArea targetWindow);

		PaintInstruction<Void> clientWindowStopDrag(
				final RenderArea targetWindow);
	}

	@Override
	public View getView();

	void startDrag();

	void stopDrag();
}
