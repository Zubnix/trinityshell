package org.trinity.render.qt.impl;

import org.trinity.foundation.display.api.DisplaySurfaceHandle;
import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.render.api.PaintableRenderNode;
import org.trinity.render.paintengine.qt.api.QJPaintContext;

import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QWidget;

public class QJPaintContextImpl implements QJPaintContext {

	private final PaintableRenderNode paintableRenderNode;
	private final QWidget visual;
	private final QJRenderEngine qjRenderEngine;

	public QJPaintContextImpl(	final PaintableRenderNode paintableRenderNode,
								final QWidget visual,
								final QJRenderEngine qjRenderEngineImpl) {
		this.paintableRenderNode = paintableRenderNode;
		this.visual = visual;
		this.qjRenderEngine = qjRenderEngineImpl;
	}

	@Override
	public QWidget getVisual() {
		return this.visual;
	}

	@Override
	public void setVisual(final QWidget qWidget) {
		this.qjRenderEngine
				.putVisual(	(DisplayEventSource) this.paintableRenderNode,
							this.paintableRenderNode,
							qWidget);
	}

	@Override
	public QWidget queryVisual(final PaintableRenderNode paintableRenderNode) {
		return this.qjRenderEngine.getVisual(paintableRenderNode);
	}

	@Override
	public DisplaySurfaceHandle createDisplaySurfaceHandle(final QWidget visual) {
		visual.setWindowFlags(WindowType.X11BypassWindowManagerHint);
		visual.setAttribute(WidgetAttribute.WA_DeleteOnClose, true);
		visual.setAttribute(WidgetAttribute.WA_DontCreateNativeAncestors, true);
		return new QJDisplaySurfaceHandle(visual);
	}
}
