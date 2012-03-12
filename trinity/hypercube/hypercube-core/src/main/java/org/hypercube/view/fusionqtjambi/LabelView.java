package org.hypercube.view.fusionqtjambi;

import org.fusion.qt.painter.QFusionPaintCall;
import org.hydrogen.api.paint.PaintContext;
import org.hyperdrive.api.widget.PaintInstruction;
import org.hyperdrive.widget.Label;

import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;

public class LabelView extends WidgetView implements Label.View {

	@Override
	public PaintInstruction<Void> onTextUpdate(final String name,
			final Object... args) {
		return new PaintInstruction<Void>(new QFusionPaintCall<Void, QLabel>() {
			@Override
			public Void call(final PaintContext<QLabel> paintContext) {
				final QLabel paintPeer = paintContext.getPaintPeer();
				paintPeer.setText(name);
				return null;
			}
		});
	}

	@Override
	protected QWidget createPaintPeer(
			final PaintContext<? extends QWidget> paintContext,
			final QWidget parentPaintPeer) {
		return new QLabel(parentPaintPeer,
				WindowType.X11BypassWindowManagerHint);
	}
}