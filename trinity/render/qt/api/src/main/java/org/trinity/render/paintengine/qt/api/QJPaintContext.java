package org.trinity.render.paintengine.qt.api;

import org.trinity.foundation.display.api.DisplaySurfaceHandle;
import org.trinity.foundation.render.api.PaintContext;
import org.trinity.foundation.render.api.PaintableRenderNode;

import com.trolltech.qt.gui.QWidget;

public interface QJPaintContext extends PaintContext {
	QWidget getVisual();

	void setVisual(QWidget qWidget);

	QWidget queryVisual(PaintableRenderNode paintableRenderNode);

	DisplaySurfaceHandle createDisplaySurfaceHandle(QWidget visual);
}
