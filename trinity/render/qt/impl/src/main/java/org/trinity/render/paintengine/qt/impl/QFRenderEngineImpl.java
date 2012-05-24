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
package org.trinity.render.paintengine.qt.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.trinity.core.display.api.ResourceHandle;
import org.trinity.core.display.api.ResourceHandleFactory;
import org.trinity.core.display.api.event.DisplayEventSource;
import org.trinity.core.render.api.PaintCalculation;
import org.trinity.core.render.api.PaintConstruction;
import org.trinity.core.render.api.PaintInstruction;
import org.trinity.core.render.api.Paintable;
import org.trinity.core.render.api.RenderEventBridge;
import org.trinity.render.paintengine.qt.api.QFRenderEngine;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.trolltech.qt.core.QCoreApplication;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;

// TODO documentation
/**
 * A <code> QFusionRenderEngine</code> is the core class for all paint related
 * operations. Every <code>QFusionPainter</code> will delegate it's paint
 * related operations to a <code>QFusionRenderEngine</code>. It's the
 * <code>QFusionRenderEngine</code>'s job to correctly handle these requests. To
 * do this, a <code>QFusionRenderEngine</code> holds a reference to every
 * registered <code>Paintable</code> and it's corresponding <code>QWidget</code>
 * paint peer. A registered <code>Paintable</code> is a <code>Paintable</code>
 * that was initially visualized by a <code>QFusionRenderEngine</code>.
 * <p>
 * A <code>QFusionRenderEngine</code> runs in a separate GUI <code>Thread</code>
 * . It is this <code>Thread</code> that will perform all visualization and
 * visual manipulation.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Singleton
public class QFRenderEngineImpl extends QApplication implements QFRenderEngine {

	private final ResourceHandleFactory resourceHandleFactory;
	private final RenderEventBridge<QEvent> renderEventBridge;
	private final Map<Paintable, QWidget> paintableToPaintPeer = new HashMap<Paintable, QWidget>();

	/**
	 * @param display
	 * @param backEndProperties
	 */
	@Inject
	protected QFRenderEngineImpl(	final ResourceHandleFactory resourceHandleFactory,
									final RenderEventBridge<QEvent> renderEventBridge) {
		super(new String[] {});
		this.resourceHandleFactory = resourceHandleFactory;
		this.renderEventBridge = renderEventBridge;
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

	/*
	 * (non-Javadoc)
	 * @see org.fusion.paintengine.api.RenderEngine#start()
	 */
	@Override
	public void start() {
		QApplication.setQuitOnLastWindowClosed(false);
		QApplication.exec();
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.paintengine.api.RenderEngine#stop()
	 */
	@Override
	public void stop() {
		QCoreApplication.quit();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.hydrogen.paint.api.RenderEngine#invoke(org.hydrogen.paint.api.Paintable
	 * , org.hydrogen.paint.api.PaintInstruction)
	 */
	@Override
	public void invoke(	final Paintable paintable,
						@SuppressWarnings("rawtypes") final PaintInstruction paintInstruction) {
		QCoreApplication.invokeLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				paintInstruction.call(paintable, QFRenderEngineImpl.this);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.hydrogen.paint.api.RenderEngine#invoke(org.hydrogen.paint.api.Paintable
	 * , org.hydrogen.paint.api.PaintConstruction)
	 */
	@Override
	public ResourceHandle invoke(	final Paintable paintable,
									@SuppressWarnings("rawtypes") final PaintConstruction paintConstruction) {
		final FutureTask<ResourceHandle> constructionTask = new FutureTask<ResourceHandle>(new Callable<ResourceHandle>() {
			@SuppressWarnings("unchecked")
			@Override
			public ResourceHandle call() throws Exception {
				return paintConstruction.construct(	paintable,
													QFRenderEngineImpl.this);
			}
		});
		QCoreApplication.invokeLater(constructionTask);
		ResourceHandle resourceHandle = null;
		try {
			resourceHandle = constructionTask.get();
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resourceHandle;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.fusion.paintengine.api.QFusionRenderEngine#getVisual(org.hydrogen
	 * .paint.api.Paintable)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends QWidget> T getVisual(final Paintable paintable) {
		return (T) this.paintableToPaintPeer.get(paintable);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.fusion.paintengine.api.QFusionRenderEngine#putVisual(org.hydrogen
	 * .paint.api.Paintable, com.trolltech.qt.gui.QWidget)
	 */
	@Override
	public ResourceHandle putVisual(final DisplayEventSource displayEventSource,
									final Paintable paintable,
									final QWidget visual) {
		this.paintableToPaintPeer.put(paintable, visual);
		visual.installEventFilter(new QFInputEventFilterImpl(	this.renderEventBridge,
																displayEventSource,
																visual));
		final long winId = visual.winId();
		return this.resourceHandleFactory.createResourceHandle(Long
				.valueOf(winId));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.hydrogen.paint.api.RenderEngine#invoke(org.hydrogen.paint.api.Paintable
	 * , org.hydrogen.paint.api.PaintCalculation)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(	final Paintable paintable,
							@SuppressWarnings("rawtypes") final PaintCalculation paintCalculation) {
		@SuppressWarnings("rawtypes")
		final FutureTask constructionTask = new FutureTask(new Callable() {
			@Override
			public Object call() throws Exception {
				return paintCalculation.calculate(	paintable,
													QFRenderEngineImpl.this);
			}
		});
		QCoreApplication.invokeLater(constructionTask);
		Object result = null;
		try {
			result = constructionTask.get();
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
