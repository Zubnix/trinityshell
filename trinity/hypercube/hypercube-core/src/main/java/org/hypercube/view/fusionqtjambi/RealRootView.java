package org.hypercube.view.fusionqtjambi;

import org.hydrogen.api.paint.PaintContext;
import org.hyperdrive.widget.BaseRoot;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QDesktopWidget;
import com.trolltech.qt.gui.QWidget;

public class RealRootView extends WidgetView implements BaseRoot.View {
	@Override
	protected QWidget createPaintPeer(
			final PaintContext<? extends QWidget> paintContext,
			final QWidget parentPaintPeer) {
		final QDesktopWidget desktopWidget = QApplication.desktop();
		desktopWidget.setFixedSize(desktopWidget.frameSize());
		return desktopWidget;
	}
}
