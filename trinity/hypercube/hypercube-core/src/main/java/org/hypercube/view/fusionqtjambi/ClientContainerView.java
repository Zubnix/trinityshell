package org.hypercube.view.fusionqtjambi;

import org.hydrogen.paintinterface.PaintContext;

import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.gui.QWidget;

public class ClientContainerView extends WidgetView {

	@Override
	protected QWidget createPaintPeer(
			final PaintContext<? extends QWidget> paintContext,
			final QWidget parentPaintPeer) {
		final QWidget paintPeer = super.createPaintPeer(paintContext,
				parentPaintPeer);
		// Disabling WA_NativeWindow should be transparent. However due to a bug
		// in RenderAreaGeoExecutor it must remain enabled for now.
		paintPeer.setAttribute(WidgetAttribute.WA_NativeWindow, true);
		return paintPeer;
	}
}
