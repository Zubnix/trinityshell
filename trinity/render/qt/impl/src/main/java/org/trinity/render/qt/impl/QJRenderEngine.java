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

import org.trinity.foundation.display.api.DisplayEventProducer;
import org.trinity.foundation.display.api.DisplaySurfaceHandle;
import org.trinity.foundation.display.api.DisplaySurfaceHandleFactory;
import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableRenderNode;
import org.trinity.render.paintengine.qt.api.QJPaintContext;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.trolltech.qt.core.QCoreApplication;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;

// TODO documentation
/**
 * A <code> QFusionRenderEngine</code> is the core class for all paint related
 * operations. Every <code>QFusionPainter</code> will delegate it's paint
 * related operations to a <code>QFusionRenderEngine</code>. It's the
 * <code>QFusionRenderEngine</code>'s job to correctly handle these requests. To
 * do this, a <code>QFusionRenderEngine</code> holds a reference to every
 * registered <code>PaintableRenderNode</code> and it's corresponding
 * <code>QWidget</code> paint peer. A registered
 * <code>PaintableRenderNode</code> is a <code>PaintableRenderNode</code> that
 * was initially visualized by a <code>QFusionRenderEngine</code>.
 * <p>
 * A <code>QFusionRenderEngine</code> runs in a separate GUI <code>Thread</code>
 * . It is this <code>Thread</code> that will perform all visualization and
 * visual manipulation.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind(to = @To(value = To.Type.CUSTOM, customs = DisplayEventProducer.class))
@Singleton
public class QJRenderEngine extends QApplication implements
		DisplayEventProducer {

	private final DisplaySurfaceHandleFactory resourceHandleFactory;
	private final QJRenderEventConverter renderEventConverter;
	private final EventBus displayEventBus;

	private final Map<PaintableRenderNode, QWidget> paintableToPaintPeer = new HashMap<PaintableRenderNode, QWidget>();

	@Inject
	QJRenderEngine(	final DisplaySurfaceHandleFactory resourceHandleFactory,
					final QJRenderEventConverter renderEventConverter,
					@Named("displayEventBus") final EventBus displayEventBus) {
		super(new String[] {});
		this.resourceHandleFactory = resourceHandleFactory;
		this.renderEventConverter = renderEventConverter;
		this.displayEventBus = displayEventBus;
	}

	@Override
	public boolean eventFilter(final QObject widget, final QEvent event) {
		if (widget.isWidgetType()) {
			if (event.type().equals(QEvent.Type.Destroy)) {
				// TODO check if this actually works.
				this.paintableToPaintPeer.values().remove(widget);
			}
		}
		return false;
	}

	@Override
	public void start() {
		QApplication.setQuitOnLastWindowClosed(false);
		QApplication.exec();
	}

	@Override
	public void stop() {
		QCoreApplication.invokeLater(new Runnable() {
			@Override
			public void run() {
				QCoreApplication.quit();
			}
		});
	}

	public <R> Future<R> invoke(final PaintableRenderNode paintableRenderNode,
								final PaintInstruction<R, QJPaintContext> paintInstruction) {
		final FutureTask<R> futureTask = new FutureTask<R>(new Callable<R>() {

			@Override
			public R call() throws Exception {
				final QWidget visual = getVisual(paintableRenderNode);
				final QJPaintContext qjPaintContext = new QJPaintContextImpl(	paintableRenderNode,
																				visual,
																				QJRenderEngine.this);
				return paintInstruction.call(	paintableRenderNode,
												qjPaintContext);
			}
		});
		QCoreApplication.invokeLater(futureTask);
		return futureTask;
	}

	public QWidget getVisual(final PaintableRenderNode paintableRenderNode) {
		return this.paintableToPaintPeer.get(paintableRenderNode);
	}

	public DisplaySurfaceHandle putVisual(	final DisplayEventSource displayEventSource,
											final PaintableRenderNode paintableRenderNode,
											final QWidget visual) {
		this.paintableToPaintPeer.put(paintableRenderNode, visual);
		visual.installEventFilter(new QJRenderEventFilter(	this.displayEventBus,
															this.renderEventConverter,
															displayEventSource,
															visual));
		final long winId = visual.winId();
		return this.resourceHandleFactory.createDisplaySurfaceHandle(Long
				.valueOf(winId));
	}
}
