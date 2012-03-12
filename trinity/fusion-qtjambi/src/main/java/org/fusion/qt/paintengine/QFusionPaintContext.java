package org.fusion.qt.paintengine;

import java.util.Map;

import org.hydrogen.api.display.event.DisplayEventSource;
import org.hydrogen.api.paint.PaintContext;
import org.hydrogen.api.paint.PaintableRef;

import com.trolltech.qt.gui.QWidget;

public class QFusionPaintContext<P extends QWidget> implements PaintContext<P> {

	private final PaintableRef paintableRef;
	private final P paintPeer;
	private final Map<PaintableRef, QWidget> paintPeers;
	private final QFusionRenderEngine renderEngine;

	protected QFusionPaintContext(final QFusionRenderEngine renderEngine,
			final PaintableRef paintableRef, final P paintPeer,
			final Map<PaintableRef, QWidget> paintPeers) {
		this.renderEngine = renderEngine;
		this.paintableRef = paintableRef;
		this.paintPeer = paintPeer;
		this.paintPeers = paintPeers;
	}

	@Override
	public P getPaintPeer() {
		return this.paintPeer;
	}

	@Override
	public PaintableRef getPaintableRef() {
		return this.paintableRef;
	}

	@Override
	public QWidget queryPaintPeer(final PaintableRef paintableRef) {
		return this.paintPeers.get(paintableRef);
	}

	@Override
	public void bindPaintPeer(final PaintableRef paintableRef, final P paintPeer) {
		this.paintPeers.put(paintableRef, paintPeer);
		paintPeer.installEventFilter(this.renderEngine);
		if (paintableRef instanceof DisplayEventSource) {
			new QFusionInputEventFilter((DisplayEventSource) paintableRef,
					paintPeer);
		}
	}
}
