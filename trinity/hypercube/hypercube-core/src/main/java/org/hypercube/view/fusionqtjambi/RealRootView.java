package org.hypercube.view.fusionqtjambi;

import org.hydrogen.api.paint.PaintContext;
import org.hyperdrive.widget.RealRoot;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QDesktopWidget;
import com.trolltech.qt.gui.QWidget;

public class RealRootView extends WidgetView implements RealRoot.View {
	@Override
	protected QWidget createPaintPeer(
			final PaintContext<? extends QWidget> paintContext,
			final QWidget parentPaintPeer) {
		final QDesktopWidget desktopWidget = QApplication.desktop();
		desktopWidget.setFixedSize(desktopWidget.frameSize());
		return desktopWidget;
	}
}
