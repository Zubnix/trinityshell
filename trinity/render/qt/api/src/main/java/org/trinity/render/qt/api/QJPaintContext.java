package org.trinity.render.qt.api;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.PaintContext;
import org.trinity.foundation.render.api.PaintableSurfaceNode;

import com.trolltech.qt.gui.QWidget;

public interface QJPaintContext extends PaintContext {

	void setVisual(QWidget qWidget);

	void disposeVisual();

	QWidget getVisual(PaintableSurfaceNode paintableSurfaceNode);

	DisplaySurface getDisplaySurface(QWidget visual);

	void syncVisualGeometryToSurfaceNode(QWidget visual);
}
