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

import org.apache.log4j.Logger;
import org.fusion.qt.painter.QFusionPaintCallBack;
import org.fusion.qt.painter.QFusionPainter;
import org.fusion.qt.painter.QFusionPainter.ReturnValueWrapper;
import org.hydrogen.displayinterface.Display;
import org.hydrogen.displayinterface.event.DisplayEventSource;
import org.hydrogen.paintinterface.Paintable;

import com.trolltech.qt.core.QCoreApplication;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.core.Qt.WindowType;
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

	public Map<Paintable, QWidget> qWidgetMap;

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
		this.qWidgetMap = new HashMap<Paintable, QWidget>();
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

	public static final String CREATE_RENDER_AREA_SLOTNAME = "createPaintArea("
			+ Paintable.class.getCanonicalName() + ","
			+ Paintable.class.getCanonicalName() + ","
			+ QFusionPaintCallBack.class.getCanonicalName() + ","
			+ "org.fusion.qt.painter.QFusionPainter$ReturnValueWrapper)";

	/**
	 * 
	 * @param parentPaintable
	 * @param paintable
	 * @param paintCall
	 * @param returnValueWrapper
	 * @throws HydrogenPaintInterfaceException
	 */
	protected void createPaintArea(final Paintable parentPaintable,
			final Paintable paintable,
			final QFusionPaintCallBack<QWidget, ? extends QWidget> paintCall,
			final ReturnValueWrapper<Long> returnValueWrapper) {

		final QWidget qWidget = paintCall.call(
				getQWidgetMap().get(parentPaintable), paintable);
		QCoreApplication.processEvents();

		final long winId = qWidget.effectiveWinId();

		// Settign this flag before asking for the effective win id, changes the
		// returned id (qt bug?)
		qWidget.setWindowFlags(WindowType.X11BypassWindowManagerHint);
		qWidget.setAttribute(WidgetAttribute.WA_DeleteOnClose, true);
		qWidget.setAttribute(WidgetAttribute.WA_DontCreateNativeAncestors, true);

		qWidget.setVisible(paintable.isVisible());

		getQWidgetMap().put(paintable, qWidget);

		qWidget.installEventFilter(this);
		if (paintable instanceof DisplayEventSource) {
			new QFusionInputEventFilter((DisplayEventSource) paintable, qWidget);
		}

		returnValueWrapper.setReturnValue(Long.valueOf(winId));
	}

	public static final String DESTROY_SLOTNAME = "destroy("
			+ Paintable.class.getCanonicalName() + ")";

	/**
	 * Destroy the visual representation of the given <code>Paintable</code>.
	 * The given <code>QFusionPainter</code> is the source object that called
	 * this method.
	 * 
	 * @param qFusionPainter
	 *            A {@link QFusionPainter}.
	 * @param paintable
	 *            A {@link Paintable}.
	 * @see {@link QFusionPainter#destroy(Paintable)}.
	 */
	protected void destroy(final Paintable paintable) {
		final QWidget renderEntityWidget = getQWidgetMap().remove(paintable);
		if (renderEntityWidget != null) {
			renderEntityWidget.close();
		}
	}

	@Override
	public boolean eventFilter(final QObject widget, final QEvent event) {
		if (widget.isWidgetType()) {
			if (event.type().equals(QEvent.Type.Destroy)) {
				// TODO check if this actually works.
				getQWidgetMap().values().remove(widget);
			}
		}
		return false;
	}

	/**
	 * A <code>Map</code> linking a <code>Paintable</code> with it's
	 * <code>QWidget</code> paint peer.
	 * 
	 * @return A <code>Map</code>.
	 */
	protected Map<Paintable, QWidget> getQWidgetMap() {
		return this.qWidgetMap;
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException();
	}

	public static final String HIDE_SLOTNAME = "hide("
			+ Paintable.class.getCanonicalName() + ")";

	/**
	 * Hide the visual representation of the given <code>Paintable</code>. The
	 * given <code>QFusionPainter</code> is the source object that called this
	 * method.
	 * 
	 * @param qFusionPainter
	 *            A {@link QFusionPainter}.
	 * @param paintable
	 *            A {@link Paintable}.
	 * @see {@link QFusionPainter#hide(Paintable)}.
	 */
	protected void hide(final Paintable paintable) {
		final QWidget widget = getQWidgetMap().get(paintable);
		if (widget != null) {
			widget.hide();
		}
	}

	public static final String LOWER_SLOTNAME = "lower("
			+ Paintable.class.getCanonicalName() + ")";

	/**
	 * Lower the visual representation of the given <code>Paintable</code>. The
	 * given <code>QFusionPainter</code> is the source object that called this
	 * method.
	 * 
	 * @param qFusionPainter
	 *            A {@link QFusionPainter}.
	 * @param paintable
	 *            A {@link Paintable}.
	 * @see {@link QFusionPainter#lower(Paintable)}.
	 */
	@SuppressWarnings("unused")
	private void lower(final Paintable paintable) {
		final QWidget widget = getQWidgetMap().get(paintable);
		if (widget != null) {
			widget.lower();
		}
	}

	public static final String MOVE_SLOTNAME = "move("
			+ Paintable.class.getCanonicalName() + ","
			+ Integer.class.getCanonicalName() + ","
			+ Integer.class.getCanonicalName() + ")";

	/**
	 * Move the visual representation of the given <code>Paintable</code>. The
	 * given <code>QFusionPainter</code> is the source object that called this
	 * method.
	 * 
	 * @param qFusionPainter
	 *            A {@link QFusionPainter}.
	 * @param paintable
	 *            A {@link Paintable}.
	 * @see {@link QFusionPainter#move(Paintable)}.
	 */
	protected void move(final Paintable paintable, final Integer x,
			final Integer y) {
		final QWidget qWidget = getQWidgetMap().get(paintable);
		if (qWidget != null) {
			qWidget.move(x.intValue(), y.intValue());
		}
	}

	public static final String MOVE_RESIZE_SLOTNAME = "moveResize("
			+ Paintable.class.getCanonicalName() + ","
			+ Integer.class.getCanonicalName() + ","
			+ Integer.class.getCanonicalName() + ","
			+ Integer.class.getCanonicalName() + ","
			+ Integer.class.getCanonicalName() + ")";

	/**
	 * Move and resize the visual representation of the given
	 * <code>Paintable</code>. The given <code>QFusionPainter</code> is the
	 * source object that called this method.
	 * 
	 * @param qFusionPainter
	 *            A {@link QFusionPainter}.
	 * @param paintable
	 *            A {@link Paintable}.
	 * @see {@link QFusionPainter#moveResize(Paintable)}.
	 */
	protected void moveResize(final Paintable paintable, final Integer x,
			final Integer y, final Integer width, final Integer height) {

		final QWidget qWidget = getQWidgetMap().get(paintable);
		if (qWidget != null) {
			qWidget.setGeometry(x.intValue(), y.intValue(), width.intValue(),
					height.intValue());
		}
	}

	public static final String PAINT_SLOTNAME = "paint("
			+ Paintable.class.getCanonicalName() + ","
			+ QFusionPaintCallBack.class.getCanonicalName() + ")";

	/**
	 * Paint the visual representation of the given <code>Paintable</code>. The
	 * given <code>QFusionPainter</code> is the source object that called this
	 * method.
	 * 
	 * @param qFusionPainter
	 *            A {@link QFusionPainter}.
	 * @param paintable
	 *            A {@link Paintable}.
	 * @see {@link QFusionPainter#paint(Paintable)}.
	 */
	protected void paint(final Paintable paintable,
			final QFusionPaintCallBack<QWidget, ?> paintCall) {
		if (paintCall != null) {
			paintCall.call(getQWidgetMap().get(paintable), paintable);
		} else {
			LOGGER.warn(String.format(NULLPAINTCALL_WARN_LOGMESSAGE, paintable));
		}
	}

	public static final String RAISE_SLOTNAME = "raise("
			+ Paintable.class.getCanonicalName() + ")";

	/**
	 * Raise the visual representation of the given <code>Paintable</code>. The
	 * given <code>QFusionPainter</code> is the source object that called this
	 * method.
	 * 
	 * @param qFusionPainter
	 *            A {@link QFusionPainter}.
	 * @param paintable
	 *            A {@link Paintable}. @ Thrown when an error occurs while
	 *            destroying the visual representation of the given
	 *            <code>Paintable</code>.
	 * @see {@link QFusionPainter#raise(Paintable)}.
	 */
	protected void raise(final Paintable paintable) {
		final QWidget widget = getQWidgetMap().get(paintable);
		if (widget != null) {
			widget.raise();
		}
	}

	public static final String RESIZE_SLOTNAME = "resize("
			+ Paintable.class.getCanonicalName() + ","
			+ Integer.class.getCanonicalName() + ","
			+ Integer.class.getCanonicalName() + ")";

	/**
	 * Resize the visual representation of the given <code>Paintable</code>. The
	 * given <code>QFusionPainter</code> is the source object that called this
	 * method.
	 * 
	 * @param qFusionPainter
	 *            A {@link QFusionPainter}.
	 * @param paintable
	 *            A {@link Paintable}. @ Thrown when an error occurs while
	 *            destroying the visual representation of the given
	 *            <code>Paintable</code>.
	 * @see {@link QFusionPainter#resize(Paintable)}.
	 */
	protected void resize(final Paintable paintable, final Integer width,
			final Integer height) {

		final QWidget qWidget = getQWidgetMap().get(paintable);
		if (qWidget != null) {
			qWidget.resize(width.intValue(), height.intValue());

		}
	}

	/**
	 * 
	 */
	public static final String SHOW_SLOTNAME = "show("
			+ Paintable.class.getCanonicalName() + ")";

	/**
	 * Show the visual representation of the given <code>Paintable</code>. The
	 * given <code>QFusionPainter</code> is the source object that called this
	 * method.
	 * 
	 * @param qFusionPainter
	 *            A {@link QFusionPainter}.
	 * @param paintable
	 *            A {@link Paintable}. @ Thrown when an error occurs while
	 *            destroying the visual representation of the given
	 *            <code>Paintable</code>.
	 * @see {@link QFusionPainter#show(Paintable)}.
	 */
	protected void show(final Paintable paintable) {
		final QWidget qWidget = getQWidgetMap().get(paintable);
		if (qWidget != null) {
			qWidget.show();
		}
	}

	/**
	 * 
	 */
	public static final String SET_PARENT_SLOTNAME = "setParent("
			+ Paintable.class.getCanonicalName() + ","
			+ Paintable.class.getCanonicalName() + ","
			+ Integer.class.getCanonicalName() + ","
			+ Integer.class.getCanonicalName() + ")";

	/**
	 * 
	 * @param paintable
	 * @param newParent
	 * @param x
	 * @param y
	 */
	protected void setParent(final Paintable paintable,
			final Paintable newParent, final Integer x, final Integer y) {
		final QWidget widget = getQWidgetMap().get(paintable);
		final QWidget parentWidget = getQWidgetMap().get(newParent);
		if ((widget != null) && (parentWidget != null)) {
			widget.setParent(parentWidget);
		}
	}

	/**
	 * 
	 */
	public static final String SET_INPUTFOCUS_SLOTNAME = "setInputFocus( "
			+ Paintable.class.getCanonicalName() + ")";

	/**
	 * 
	 * @param paintable
	 */
	protected void setInputFocus(final Paintable paintable) {
		final QWidget widget = getQWidgetMap().get(paintable);
		if (widget != null) {
			widget.setFocus();
		}
	}
}
