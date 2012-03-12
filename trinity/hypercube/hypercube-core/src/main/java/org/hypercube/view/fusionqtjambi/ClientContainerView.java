package org.hypercube.view.fusionqtjambi;

import org.hydrogen.api.paint.PaintContext;

import com.trolltech.qt.gui.QWidget;

public class ClientContainerView extends WidgetView {

	@Override
	protected QWidget createPaintPeer(
			final PaintContext<? extends QWidget> paintContext,
			final QWidget parentPaintPeer) {
		final QWidget paintPeer = super.createPaintPeer(paintContext,
				parentPaintPeer);
		// paintPeer.setAttribute(WidgetAttribute.WA_NativeWindow, true);
		return paintPeer;
	}
}
