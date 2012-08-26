package org.trinity.render.paintengine.qt.api;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.PaintContext;
import org.trinity.foundation.render.api.PaintableRenderNode;

import com.trolltech.qt.gui.QWidget;

public interface QJPaintContext extends PaintContext {
	QWidget getVisual();

	void setVisual(QWidget qWidget);

	void evictVisual();

	QWidget queryVisual(PaintableRenderNode paintableRenderNode);

	DisplaySurface getDisplaySurface(QWidget visual);

	void syncVisualGeometryToNode(	QWidget visual,
									PaintableRenderNode paintableRenderNode);
}
