/*
 * This file is part of Fusion-qtjambi.
 * 
 * Fusion-qtjambi is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * Fusion-qtjambi is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-qtjambi. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.qt.paintengine;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.apache.log4j.Logger;
import org.fusion.qt.painter.QFusionPaintCall;
import org.fusion.qt.painter.QFusionPainter;
import org.hydrogen.display.api.Display;
import org.hydrogen.paint.api.Paintable;
import org.hydrogen.paint.api.PaintableRef;
import org.hydrogen.paint.api.base.BasePaintableRef;

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
public class QFusionRenderEngine extends QApplication {

	private static final Logger LOGGER = Logger
			.getLogger(QFusionRenderEngine.class);
	private static final String NULLPAINTCALL_WARN_LOGMESSAGE = "Received null PaintCall for Paintable: %s";

	public Map<PaintableRef, QWidget> qWidgetMap;

	private static ThreadLocal<QFusionEventProducer> THREADLOCAL_EVENT_PRODUCER;

	/**
	 * 
	 * @param display
	 * @param backEndProperties
	 */
	protected QFusionRenderEngine(final Display display,
			final String[] backEndProperties) {
		super(backEndProperties);
		// FIXME we are using a dynamic display instance in a static variable.
		// Not good!
		QFusionRenderEngine.THREADLOCAL_EVENT_PRODUCER = new ThreadLocal<QFusionEventProducer>() {
			@Override
			protected QFusionEventProducer initialValue() {
				final QFusionEventProducer promotor = QFusionEventProducerFactory
						.getCreatedEventPromotor(display);
				return promotor;
			}
		};
		this.qWidgetMap = new HashMap<PaintableRef, QWidget>();
	}

	/**
	 * Returns the thread local </code>QFusionEventPromtor</code>. Only a main
	 * gui thread has a <code>QFusionEventPromotor</code> associated with it.
	 * <p>
	 * This method is intended to be called from a <code>QWidget</code>.
	 * <p>
	 * This method returns null if called by non main gui thread.
	 * 
	 * @return
	 */
	public static QFusionEventProducer getEventPromotor() {
		return QFusionRenderEngine.THREADLOCAL_EVENT_PRODUCER.get();
	}

	@Override
	public boolean eventFilter(final QObject widget, final QEvent event) {
		if (widget.isWidgetType()) {
			if (event.type().equals(QEvent.Type.Destroy)) {
				// TODO check if this actually works.
				this.qWidgetMap.values().remove(widget);
			}
		}
		return false;
	}

	/**
	 * Paint the visual representation of the given <code>Paintable</code>. The
	 * given <code>QFusionPainter</code> is the source object that called this
	 * method.
	 * 
	 * @param qFusionPainter
	 *            A {@link QFusionPainter}.
	 * @param paintable
	 *            A {@link Paintable}.
	 * @see {@link QFusionPainter#paintAsync(Paintable)}.
	 */
	public <R, P extends QWidget> Future<R> invoke(final Paintable paintable,
			final QFusionPaintCall<R, P> paintCall) {
		if (paintCall != null) {
			final PaintableRef paintableRef = new BasePaintableRef(paintable);
			final FutureTask<R> futureTask = new FutureTask<R>(
					new Callable<R>() {
						@Override
						public R call() throws Exception {
							@SuppressWarnings("unchecked")
							final P paintPeer = (P) QFusionRenderEngine.this.qWidgetMap
									.get(paintableRef);
							final QFusionPaintContext<P> paintContext = new QFusionPaintContext<P>(
									QFusionRenderEngine.this, paintableRef,
									paintPeer,
									QFusionRenderEngine.this.qWidgetMap);
							return paintCall.call(paintContext);
						}
					});
			QCoreApplication.invokeLater(futureTask);
			return futureTask;
		} else {
			QFusionRenderEngine.LOGGER.warn(String.format(
					QFusionRenderEngine.NULLPAINTCALL_WARN_LOGMESSAGE,
					paintable));
			return null;
		}
	}
}
