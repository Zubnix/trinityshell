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
import java.util.concurrent.atomic.AtomicBoolean;

import org.trinity.foundation.display.api.DisplayEventProducer;
import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableRenderNode;
import org.trinity.render.paintengine.qt.api.QJPaintContext;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.trolltech.qt.core.QCoreApplication;
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
@Bind(multiple = true, to = @To(value = To.Type.CUSTOM, customs = DisplayEventProducer.class))
@Singleton
public class QJRenderEngine implements DisplayEventProducer, Runnable {

	private final Map<PaintableRenderNode, QWidget> paintableToPaintPeer = new HashMap<PaintableRenderNode, QWidget>();
	private final AtomicBoolean initialized = new AtomicBoolean(false);

	private final QJRenderEventConverter renderEventConverter;
	private final EventBus displayEventBus;

	private Thread renderThread;

	@Inject
	QJRenderEngine(	final QJRenderEventConverter renderEventConverter,
					@Named("displayEventBus") final EventBus displayEventBus) {
		this.renderEventConverter = renderEventConverter;
		this.displayEventBus = displayEventBus;
	}

	public void removeVisual(final PaintableRenderNode paintableRenderNode) {
		this.paintableToPaintPeer.remove(paintableRenderNode);
	}

	@Override
	public void startDisplayEventProduction() {
		this.renderThread = new Thread(	this,
										"Qt Jambi Render Thread");
		this.renderThread.start();
	}

	@Override
	public void stopDisplayEventProduction() {
		QCoreApplication.invokeLater(new Runnable() {
			@Override
			public void run() {
				QCoreApplication.quit();
				QJRenderEngine.this.initialized.set(false);
			}
		});
	}

	public <R> Future<R> invoke(final PaintableRenderNode paintableRenderNode,
								final PaintInstruction<R, QJPaintContext> paintInstruction) {
		// make sure qapplication.initialize() is finished before submitting any
		// runnables, else they get lost.
		while (!this.initialized.get()) {
			synchronized (this) {
				try {
					wait();
				} catch (final InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

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

	public void putVisual(	final DisplayEventSource displayEventSource,
							final PaintableRenderNode paintableRenderNode,
							final QWidget visual) {
		visual.installEventFilter(new QJRenderEventFilter(	this.displayEventBus,
															this.renderEventConverter,
															displayEventSource,
															visual));
		this.paintableToPaintPeer.put(	paintableRenderNode,
										visual);
	}

	@Override
	public void run() {
		QApplication.initialize(new String[] {});
		this.initialized.set(true);
		synchronized (this) {
			notifyAll();
		}
		QApplication.setQuitOnLastWindowClosed(false);
		QApplication.exec();
	}
}
