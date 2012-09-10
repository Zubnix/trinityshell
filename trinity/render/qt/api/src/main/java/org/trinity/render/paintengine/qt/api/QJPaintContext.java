package org.trinity.render.paintengine.qt.api;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.PaintContext;
import org.trinity.foundation.render.api.PaintableSurfaceNode;

import com.trolltech.qt.gui.QWidget;

public interface QJPaintContext extends PaintContext {
	QWidget getRootVisual();

	void setVisual(QWidget qWidget);

	void evictVisual();

	QWidget queryVisual(PaintableSurfaceNode paintableSurfaceNode);

	DisplaySurface getDisplaySurface(QWidget visual);

	void syncVisualGeometryToNode(	QWidget visual,
									PaintableSurfaceNode paintableSurfaceNode);
}
