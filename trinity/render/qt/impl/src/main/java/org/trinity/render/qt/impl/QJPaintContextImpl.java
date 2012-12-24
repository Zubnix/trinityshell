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

import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.render.qt.api.QJPaintContext;

import com.google.common.eventbus.EventBus;
import com.trolltech.qt.gui.QWidget;

public class QJPaintContextImpl implements QJPaintContext {

	// private final QJRenderEventConverter renderEventConverter;
	// private final EventBus displayEventBus;
	private final PaintableSurfaceNode paintableSurfaceNode;

	public QJPaintContextImpl(	QJRenderEventConverter renderEventConverter,
								EventBus displayEventBus,
								final PaintableSurfaceNode paintableSurfaceNode) {
		this.paintableSurfaceNode = paintableSurfaceNode;
		// this.renderEventConverter = renderEventConverter;
		// this.displayEventBus = displayEventBus;
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
	public PaintableSurfaceNode getPaintableSurfaceNode() {
		return this.paintableSurfaceNode;
	}

//	@Override
//	public QJViewEventSubscription subscribeToEvents(	final DisplayEventTarget displayEventSource,
//														final QWidget view) {
//		QObject eventFilter = new QJViewEventTracker(	this.displayEventBus,
//														this.renderEventConverter,
//														displayEventSource,
//														view);
//		view.installEventFilter(eventFilter);
//		return new QJViewEventSubscriptionImpl(	view,
//												eventFilter);
//	}
}
