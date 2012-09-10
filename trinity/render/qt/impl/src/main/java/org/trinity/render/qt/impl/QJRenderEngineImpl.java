/*
 * This file is part of Fusion-qtjambi. Fusion-qtjambi is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Fusion-qtjambi is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with Fusion-qtjambi. If not,
 * see <http://www.gnu.org/licenses/>.
 */
package org.trinity.render.qt.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.trinity.foundation.display.api.DisplaySurfaceFactory;
import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.render.paintengine.qt.api.QJPaintContext;
import org.trinity.render.paintengine.qt.api.QJRenderEngine;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.trolltech.qt.core.QCoreApplication;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
/**
 * A <code> QFusionRenderEngine</code> is the core class for all paint related
 * operations. Every <code>QFusionPainter</code> will delegate it's paint
 * related operations to a <code>QFusionRenderEngine</code>. It's the
 * <code>QFusionRenderEngine</code>'s job to correctly handle these requests. To
 * do this, a <code>QFusionRenderEngine</code> holds a reference to every
 * registered <code>PaintableSurfaceNode</code> and it's corresponding
 * <code>QWidget</code> paint peer. A registered
 * <code>PaintableSurfaceNode</code> is a <code>PaintableSurfaceNode</code> that
 * was initially visualized by a <code>QFusionRenderEngine</code>.
 * <p>
 * A <code>QFusionRenderEngine</code> runs in a separate GUI <code>Thread</code>
 * . It is this <code>Thread</code> that will perform all visualization and
 * visual manipulation.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind
@Singleton
public class QJRenderEngineImpl implements QJRenderEngine {

	private final Map<PaintableSurfaceNode, QWidget> paintableToPaintPeer = new HashMap<PaintableSurfaceNode, QWidget>();

	private final QJRenderEventConverter renderEventConverter;
	private final DisplaySurfaceFactory displaySurfaceFactory;
	private final EventBus displayEventBus;

	@Inject
	QJRenderEngineImpl(	final QJRenderEventConverter renderEventConverter,
						final DisplaySurfaceFactory displaySurfaceFactory,
						@Named("displayEventBus") final EventBus displayEventBus) {
		this.renderEventConverter = renderEventConverter;
		this.displaySurfaceFactory = displaySurfaceFactory;
		this.displayEventBus = displayEventBus;
	}

	public void removeVisual(final PaintableSurfaceNode paintableSurfaceNode) {
		this.paintableToPaintPeer.remove(paintableSurfaceNode);
	}

	@Override
	public <R> Future<R> invoke(final PaintableSurfaceNode paintableSurfaceNode,
								final PaintInstruction<R, QJPaintContext> paintInstruction) {
		// make sure qapplication.initialize() is finished before submitting any

		// runnables, else they get lost.
		while (QCoreApplication.startingUp()) {
			synchronized (this) {
				try {
					wait(25);
					Thread.yield();
				} catch (final InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		final FutureTask<R> futureTask = new FutureTask<R>(new Callable<R>() {
			@Override
			public R call() throws Exception {
				final QWidget visual = getVisual(paintableSurfaceNode);
				final QJPaintContext qjPaintContext = new QJPaintContextImpl(	paintableSurfaceNode,
																				visual,
																				QJRenderEngineImpl.this,
																				QJRenderEngineImpl.this.displaySurfaceFactory);
				final R result = paintInstruction.call(	paintableSurfaceNode,
														qjPaintContext);
				return result;
			}
		});
		QCoreApplication.invokeLater(futureTask);
		return futureTask;
	}

	public QWidget getVisual(final PaintableSurfaceNode paintableSurfaceNode) {
		return this.paintableToPaintPeer.get(paintableSurfaceNode);
	}

	public void putVisual(	final DisplayEventSource displayEventSource,
							final PaintableSurfaceNode paintableSurfaceNode,
							final QWidget visual) {
		visual.installEventFilter(new QJRenderEventFilter(	this.displayEventBus,
															this.renderEventConverter,
															displayEventSource,
															visual));
		this.paintableToPaintPeer.put(	paintableSurfaceNode,
										visual);
	}

}
