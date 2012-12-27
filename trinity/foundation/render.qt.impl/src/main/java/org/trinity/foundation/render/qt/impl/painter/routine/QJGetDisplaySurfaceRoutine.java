package org.trinity.foundation.render.qt.impl.painter.routine;

import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.DisplaySurfaceFactory;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.render.PaintContext;
import org.trinity.foundation.api.render.PaintRoutine;
import org.trinity.foundation.render.qt.impl.QJDisplaySurfaceHandle;

import com.trolltech.qt.gui.QWidget;

public class QJGetDisplaySurfaceRoutine implements PaintRoutine<DisplaySurface, PaintContext> {

	private final QWidget view;
	private final DisplaySurfaceFactory displaySurfaceFactory;

	public QJGetDisplaySurfaceRoutine(final DisplaySurfaceFactory displaySurfaceFactory, final QWidget view) {

		this.view = view;
		this.displaySurfaceFactory = displaySurfaceFactory;
	}

	@Override
	public DisplaySurface call(final PaintContext paintContext) {

		final DisplaySurfaceHandle displaySurfaceHandle = new QJDisplaySurfaceHandle(this.view);
		final DisplaySurface displaySurface = this.displaySurfaceFactory.createDisplaySurface(displaySurfaceHandle);

		return displaySurface;
	}
}