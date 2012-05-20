package org.hypercube.view.fusionqtjambi;

import org.hydrogen.paint.api.PaintContext;
import org.hyperdrive.widget.api.Root;
import org.hyperdrive.widget.api.ViewImplementation;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QDesktopWidget;
import com.trolltech.qt.gui.QWidget;

@ViewImplementation(Root.View.class)
public class RealRootView extends WidgetView implements Root.View {
	@Override
	protected QWidget createPaintPeer(
			final PaintContext<? extends QWidget> paintContext,
			final QWidget parentPaintPeer) {
		final QDesktopWidget desktopWidget = QApplication.desktop();
		desktopWidget.setFixedSize(desktopWidget.frameSize());
		return desktopWidget;
	}
}
