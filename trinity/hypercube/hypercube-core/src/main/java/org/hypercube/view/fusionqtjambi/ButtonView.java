package org.hypercube.view.fusionqtjambi;

import org.fusion.qt.painter.QFusionPaintCall;
import org.hydrogen.api.display.input.MouseInput;
import org.hydrogen.api.paint.PaintContext;
import org.hyperdrive.api.widget.PaintInstruction;
import org.hyperdrive.widget.BaseButton;

import com.trolltech.qt.gui.QWidget;

public class ButtonView extends WidgetView implements Button.View {

	public static final PaintInstruction<Void> MOUSE_BUTTON_PRESSED_INSTRUCTION = new PaintInstruction<Void>(
			new QFusionPaintCall<Void, QWidget>() {
				@Override
				public Void call(final PaintContext<QWidget> paintContext) {
					final QWidget paintPeer = paintContext.getPaintPeer();
					paintPeer.setProperty("pressed", true);
					paintPeer.style().unpolish(paintPeer);
					paintPeer.style().polish(paintPeer);
					return null;
				}
			});

	public static final PaintInstruction<Void> MOUSE_BUTTON_RELEASED_INSTRUCTION = new PaintInstruction<Void>(
			new QFusionPaintCall<Void, QWidget>() {
				@Override
				public Void call(final PaintContext<QWidget> paintContext) {
					final QWidget paintPeer = paintContext.getPaintPeer();
					paintPeer.setProperty("pressed", false);
					paintPeer.style().unpolish(paintPeer);
					paintPeer.style().polish(paintPeer);
					return null;
				}
			});

	@Override
	public PaintInstruction<Void> mouseButtonPressed(final MouseInput input) {
		return ButtonView.MOUSE_BUTTON_PRESSED_INSTRUCTION;
	}

	@Override
	public PaintInstruction<Void> mouseButtonReleased(final MouseInput input) {
		return ButtonView.MOUSE_BUTTON_RELEASED_INSTRUCTION;
	}

}
