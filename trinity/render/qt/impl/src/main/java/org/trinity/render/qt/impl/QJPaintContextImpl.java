/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.render.qt.impl;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.display.api.DisplaySurfaceFactory;
import org.trinity.foundation.display.api.DisplaySurfaceHandle;
import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.render.qt.api.QJPaintContext;

import com.trolltech.qt.gui.QWidget;

public class QJPaintContextImpl implements QJPaintContext {

	private final PaintableSurfaceNode paintableSurfaceNode;
	private final QJRenderEngineImpl qjRenderEngine;
	private final DisplaySurfaceFactory displaySurfaceFactory;

	public QJPaintContextImpl(	final PaintableSurfaceNode paintableSurfaceNode,
								final QJRenderEngineImpl qjRenderEngineImpl,
								final DisplaySurfaceFactory displaySurfaceFactory) {
		this.paintableSurfaceNode = paintableSurfaceNode;
		this.qjRenderEngine = qjRenderEngineImpl;
		this.displaySurfaceFactory = displaySurfaceFactory;
	}

	@Override
	public void setVisual(final QWidget qWidget) {
		this.qjRenderEngine.putVisual(	(DisplayEventSource) this.paintableSurfaceNode,
										this.paintableSurfaceNode,
										qWidget);
	}

	@Override
	public QWidget getVisual(final PaintableSurfaceNode paintableSurfaceNode) {
		return this.qjRenderEngine.getVisual(paintableSurfaceNode);
	}

	@Override
	public DisplaySurface getDisplaySurface(final QWidget visual) {
		if (visual == null) {
			return null;
		}
		final DisplaySurfaceHandle displaySurfaceHandle = new QJDisplaySurfaceHandle(visual);
		final DisplaySurface displaySurface = this.displaySurfaceFactory.createDisplaySurface(displaySurfaceHandle);

		return displaySurface;
	}

	@Override
	public void syncVisualGeometryToSurfaceNode(final QWidget visual) {
		final int x = this.paintableSurfaceNode.getX();
		final int y = this.paintableSurfaceNode.getY();
		final int width = this.paintableSurfaceNode.getWidth();
		final int height = this.paintableSurfaceNode.getHeight();

		final boolean visible = this.paintableSurfaceNode.isVisible();

		visual.setGeometry(	x,
							y,
							width,
							height);
		visual.setVisible(visible);
	}

	@Override
	public void disposeVisual() {
		this.qjRenderEngine.removeVisual(this.paintableSurfaceNode);
	}

	@Override
	public PaintableSurfaceNode getPaintableSurfaceNode() {
		return this.paintableSurfaceNode;
	}
}
