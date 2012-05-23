package org.hypercube.view.fusionqtjambi;

import org.fusion.paintengine.impl.painter.QFusionPaintCall;
import org.hyperdrive.widget.api.Button;
import org.hyperdrive.widget.api.ViewImplementation;
import org.trinity.core.input.api.MouseInput;
import org.trinity.core.render.api.PaintContext;

import com.trolltech.qt.gui.QWidget;

@ViewImplementation(Button.View.class)
public class ButtonView extends WidgetView implements Button.View {

	public static final QFusionPaintCall<Void, QWidget> MOUSE_BUTTON_PRESSED_INSTRUCTION = new QFusionPaintCall<Void, QWidget>() {
		@Override
		public Void call(final PaintContext<QWidget> paintContext) {
			final QWidget paintPeer = paintContext.getPaintPeer();
			paintPeer.setProperty("pressed", true);
			paintPeer.style().unpolish(paintPeer);
			paintPeer.style().polish(paintPeer);
			return null;
		}
	};

	public static final QFusionPaintCall<Void, QWidget> MOUSE_BUTTON_RELEASED_INSTRUCTION = new QFusionPaintCall<Void, QWidget>() {
		@Override
		public Void call(final PaintContext<QWidget> paintContext) {
			final QWidget paintPeer = paintContext.getPaintPeer();
			paintPeer.setProperty("pressed", false);
			paintPeer.style().unpolish(paintPeer);
			paintPeer.style().polish(paintPeer);
			return null;
		}
	};

	@Override
	public QFusionPaintCall<Void, QWidget> mouseButtonPressed(
			final MouseInput input) {
		return ButtonView.MOUSE_BUTTON_PRESSED_INSTRUCTION;
	}

	@Override
	public QFusionPaintCall<Void, QWidget> mouseButtonReleased(
			final MouseInput input) {
		return ButtonView.MOUSE_BUTTON_RELEASED_INSTRUCTION;
	}

}
