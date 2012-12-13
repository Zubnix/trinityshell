package org.trinity.render.qt.impl.painter.instructions;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.display.api.DisplaySurfaceFactory;
import org.trinity.foundation.display.api.DisplaySurfaceHandle;
import org.trinity.foundation.render.api.PaintRoutine;
import org.trinity.render.qt.api.QJPaintContext;
import org.trinity.render.qt.impl.QJDisplaySurfaceHandle;

import com.trolltech.qt.gui.QWidget;

public class QJGetDisplaySurfaceRoutine implements PaintRoutine<DisplaySurface, QJPaintContext> {

	private final QWidget view;
	private final DisplaySurfaceFactory displaySurfaceFactory;

	public QJGetDisplaySurfaceRoutine(DisplaySurfaceFactory displaySurfaceFactory, QWidget view) {

		this.view = view;
		this.displaySurfaceFactory = displaySurfaceFactory;
	}

	@Override
	public DisplaySurface call(QJPaintContext paintContext) {

		final DisplaySurfaceHandle displaySurfaceHandle = new QJDisplaySurfaceHandle(view);
		final DisplaySurface displaySurface = this.displaySurfaceFactory.createDisplaySurface(displaySurfaceHandle);

		return displaySurface;
	}
}