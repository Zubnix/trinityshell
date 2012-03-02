package org.hypercube.view.fusionqtjambi;

import org.fusion.qt.painter.QFusionPaintCall;
import org.fusion.x11.core.XResourceHandle;
import org.hydrogen.displayinterface.ResourceHandle;
import org.hydrogen.paintinterface.PaintContext;
import org.hydrogen.paintinterface.Paintable;
import org.hyperdrive.widget.PaintInstruction;
import org.hyperdrive.widget.Widget;

import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QWidget;

public class WidgetView implements Widget.View {

	@Override
	public PaintInstruction<ResourceHandle> doCreate(
			final Paintable parentPaintable, final Object... args) {
		return new PaintInstruction<ResourceHandle>(
				new QFusionPaintCall<ResourceHandle, QWidget>() {
					@Override
					public ResourceHandle call(
							final PaintContext<QWidget> paintContext) {
						final QWidget parentPaintPeer = (QWidget) paintContext
								.queryPaintPeer(parentPaintable);
						final QWidget paintPeer = createPaintPeer(paintContext,
								parentPaintPeer);
						return preperatePaintPeer(paintContext, paintPeer);
					}
				});
	}

	protected QWidget createPaintPeer(
			final PaintContext<? extends QWidget> paintContext,
			final QWidget parentPaintPeer) {

		// we instantiate all visuals as a qframe for more styling options.
		final QWidget paintPeer = new QFrame(parentPaintPeer);
		return paintPeer;
	}

	protected ResourceHandle preperatePaintPeer(
			final PaintContext<QWidget> paintContext, final QWidget newPaintPeer) {

		final long winId = newPaintPeer.effectiveWinId();

		newPaintPeer.setWindowFlags(WindowType.X11BypassWindowManagerHint);
		newPaintPeer.setAttribute(WidgetAttribute.WA_DeleteOnClose, true);
		newPaintPeer.setAttribute(WidgetAttribute.WA_DontCreateNativeAncestors,
				true);

		final Paintable paintable = paintContext.getPaintable();

		newPaintPeer.setVisible(paintable.isVisible());
		paintContext.bindPaintPeer(paintable, newPaintPeer);

		// set the styling id to the full classname of the paintable.
		final String name = paintable.getClass().getSimpleName();
		newPaintPeer.setObjectName(name);

		// TODO platform independent resource handle
		final ResourceHandle resourceHandle = XResourceHandle.valueOf(Long
				.valueOf(winId));

		return resourceHandle;
	}
}