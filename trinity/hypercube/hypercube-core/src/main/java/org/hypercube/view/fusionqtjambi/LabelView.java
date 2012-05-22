package org.hypercube.view.fusionqtjambi;

import org.fusion.paintengine.impl.painter.QFusionPaintCall;
import org.hydrogen.paint.api.PaintContext;
import org.hyperdrive.widget.api.Label;
import org.hyperdrive.widget.api.ViewImplementation;

import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;

@ViewImplementation(Label.View.class)
public class LabelView extends WidgetView implements Label.View {

	@Override
	public QFusionPaintCall<Void, QLabel> labelUpdated(final String labelValue) {
		return new QFusionPaintCall<Void, QLabel>() {
			@Override
			public Void call(final PaintContext<QLabel> paintContext) {
				final QLabel paintPeer = paintContext.getPaintPeer();
				paintPeer.setText(labelValue);
				return null;
			}
		};
	}

	@Override
	protected QWidget createPaintPeer(
			final PaintContext<? extends QWidget> paintContext,
			final QWidget parentPaintPeer) {
		return new QLabel(parentPaintPeer,
				WindowType.X11BypassWindowManagerHint);
	}
}