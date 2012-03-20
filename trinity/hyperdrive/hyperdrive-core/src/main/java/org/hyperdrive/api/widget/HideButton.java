package org.hyperdrive.api.widget;

import org.hyperdrive.api.core.RenderArea;

public interface HideButton extends Button, RectangleManipulator {

	public interface View extends Button.View, RectangleManipulator.View {
		PaintInstruction<Void> clientWindowHidden(final RenderArea targetWindow);
	}

	@Override
	public View getView();

	void hideClientWindow();
}
