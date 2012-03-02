package org.fusion.qt.paintengine;

import java.util.Map;

import org.hydrogen.displayinterface.event.DisplayEventSource;
import org.hydrogen.paintinterface.PaintContext;
import org.hydrogen.paintinterface.Paintable;

import com.trolltech.qt.gui.QWidget;

public class QFusionPaintContext<P extends QWidget> implements PaintContext<P> {

	private final Paintable paintable;
	private final P paintPeer;
	private final Map<Paintable, QWidget> paintPeers;
	private final QFusionRenderEngine renderEngine;

	protected QFusionPaintContext(final QFusionRenderEngine renderEngine,
			final Paintable paintable, final P paintPeer,
			final Map<Paintable, QWidget> paintPeers) {
		this.renderEngine = renderEngine;
		this.paintable = paintable;
		this.paintPeer = paintPeer;
		this.paintPeers = paintPeers;
	}

	@Override
	public P getPaintPeer() {
		return this.paintPeer;
	}

	@Override
	public Paintable getPaintable() {
		return this.paintable;
	}

	@Override
	public QWidget queryPaintPeer(final Paintable paintable) {
		return this.paintPeers.get(paintable);
	}

	@Override
	public void bindPaintPeer(final Paintable paintable, final P paintPeer) {
		this.paintPeers.put(paintable, paintPeer);
		paintPeer.installEventFilter(this.renderEngine);
		if (paintable instanceof DisplayEventSource) {
			new QFusionInputEventFilter((DisplayEventSource) paintable,
					paintPeer);
		}
	}
}
