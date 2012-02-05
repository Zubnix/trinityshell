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
package org.fusion.qt.painter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.fusion.qt.error.InterruptedPaintResourceInitialization;
import org.fusion.qt.paintengine.QFusionRenderEngine;
import org.fusion.x11.core.XResourceHandle;
import org.hydrogen.displayinterface.ResourceHandle;

import org.hydrogen.paintinterface.PaintCall;
import org.hydrogen.paintinterface.Paintable;
import org.hydrogen.paintinterface.Painter;

import com.trolltech.qt.QSignalEmitter;
import com.trolltech.qt.core.Qt.ConnectionType;
import com.trolltech.qt.gui.QWidget;

/**
 * A <code>QFusionPainter</code> is the <code>Painter</code> implementation for
 * the Q Fusion paint system. Calls that are made to <code>QFusionPainter</code>
 * are delegated through QtJambi Signals to the <code>QFusionRenderEngine</code>
 * .
 * <p>
 * These delegated methods are not performed by the calling <code>Thread</code>
 * but are placed on a queue and will be performed by the
 * <code>QFusionRenderEngine</code> GUI <code>Thread</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class QFusionPainter extends QSignalEmitter implements Painter {

	private static final Logger LOGGER = Logger.getLogger(QFusionPainter.class);
	private static final String PAINT_RESOURCE_LOGMESSAGE = "Error while initializing paint peer resource.";

	/**
	 * A <code>ReturnValueWrapper</code> is a container for returning a thread
	 * safe asynchronous computation.
	 * <p>
	 * The <code>Thread</code> that is expecting a result calls
	 * <code>getReturnValue()</code>. This method will block until a result is
	 * available. The result can be set by the computing <code>Thread</code> by
	 * calling <code>setReturnValue(Object)</code>.
	 * <p>
	 * Note that only after a result is retrieved that a new result can be set.
	 * If a result has not yet been retrieved the setting <code>Thread</code>
	 * will block.
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 * @param <T>
	 *            The type of the result that should be set and retrieved.
	 */
	public static final class ReturnValueWrapper<T> {
		private final BlockingQueue<T> valueContainer;
		private static final int TIME_OUT = 10;
		private T value;

		/**
		 * Create a new <code>ReturnValueWrapper</code> with a default time-out
		 * of 10 seconds for the <code>Thread</code> that will set the return
		 * value.
		 */
		private ReturnValueWrapper() {
			this.valueContainer = new ArrayBlockingQueue<T>(1);
		}

		/**
		 * 
		 * @return
		 * @throws InterruptedException
		 */
		public T getReturnValue() throws InterruptedException {

			if (this.value == null) {
				this.value = this.valueContainer.take();
			}

			return this.value;
		}

		/**
		 * Set the computed return value for this
		 * <code>ReturnValueWrapper</code>.
		 * 
		 * @param returnValue
		 *            A return value.
		 */
		public void setReturnValue(final T returnValue) {

			try {
				if (!this.valueContainer.offer(returnValue, TIME_OUT,
						TimeUnit.SECONDS)) {
					throw new RuntimeException(String.format(
							"Time Out.\nCould not set %s for %s", returnValue,
							this));
				}
			} catch (final InterruptedException e) {

				throw new RuntimeException(
						String.format(
								"Interrupted while waiting for free queue space.\nCould not set %s for %s",
								returnValue, this), e);
			}

		}
	}

	private final Signal4<Paintable, Paintable, QFusionPaintCallBack<? extends QWidget, ?>, ReturnValueWrapper<Long>> createRenderAreaSignal;
	private final Signal1<Paintable> destroySignal;
	private final Signal1<Paintable> hideSignal;
	private final Signal5<Paintable, Integer, Integer, Integer, Integer> moveResizeSignal;
	private final Signal3<Paintable, Integer, Integer> moveSignal;
	private final Signal2<Paintable, QFusionPaintCallBack<? extends QWidget, ?>> backendWorkSignal;

	private final Signal3<Paintable, Integer, Integer> resizeSignal;
	private final Signal1<Paintable> showSignal;

	private final Signal1<Paintable> raiseSignal;
	private final Signal1<Paintable> lowerSignal;

	private final Signal4<Paintable, Paintable, Integer, Integer> setParentSignal;
	private final Signal1<Paintable> setInputFocus;

	private final QFusionRenderEngine qFusionRenderEngine;
	private final Paintable paintable;

	/**
	 * Create a new <code>QFusionPainter</code> that will delegate all it's
	 * public method calls to the given <code>QFusionRenderEngine</code>.
	 * 
	 * @param qFusionRenderEngine
	 *            A <code>QFusionRenderEngine</code> responsible for handling
	 *            all delegated public method calls from the created
	 *            <code>QFusionPainter</code>.
	 */
	protected QFusionPainter(final QFusionRenderEngine qFusionRenderEngine,
			final Paintable paintable) {
		// TODO only use backendWorkSignal, implement other signals with calls.
		this.qFusionRenderEngine = qFusionRenderEngine;
		this.paintable = paintable;
		{
			this.createRenderAreaSignal = new Signal4<Paintable, Paintable, QFusionPaintCallBack<? extends QWidget, ?>, ReturnValueWrapper<Long>>();
			this.createRenderAreaSignal.connect(qFusionRenderEngine,
					QFusionRenderEngine.CREATE_RENDER_AREA_SLOTNAME,
					ConnectionType.BlockingQueuedConnection);
		}
		{
			this.destroySignal = new Signal1<Paintable>();
			this.destroySignal.connect(qFusionRenderEngine,
					QFusionRenderEngine.DESTROY_SLOTNAME);
		}
		{
			this.hideSignal = new Signal1<Paintable>();
			this.hideSignal.connect(qFusionRenderEngine,
					QFusionRenderEngine.HIDE_SLOTNAME);
		}
		{
			this.moveResizeSignal = new Signal5<Paintable, Integer, Integer, Integer, Integer>();
			this.moveResizeSignal.connect(qFusionRenderEngine,
					QFusionRenderEngine.MOVE_RESIZE_SLOTNAME);
		}
		{
			this.moveSignal = new Signal3<Paintable, Integer, Integer>();
			this.moveSignal.connect(qFusionRenderEngine,
					QFusionRenderEngine.MOVE_SLOTNAME);
		}
		{
			this.backendWorkSignal = new Signal2<Paintable, QFusionPaintCallBack<? extends QWidget, ?>>();
			this.backendWorkSignal.connect(qFusionRenderEngine,
					QFusionRenderEngine.PAINT_SLOTNAME);
		}
		{
			this.resizeSignal = new Signal3<Paintable, Integer, Integer>();
			this.resizeSignal.connect(qFusionRenderEngine,
					QFusionRenderEngine.RESIZE_SLOTNAME);
		}
		{
			this.showSignal = new Signal1<Paintable>();
			this.showSignal.connect(qFusionRenderEngine,
					QFusionRenderEngine.SHOW_SLOTNAME);
		}

		{
			this.raiseSignal = new Signal1<Paintable>();
			this.raiseSignal.connect(qFusionRenderEngine,
					QFusionRenderEngine.RAISE_SLOTNAME);
		}
		{
			this.lowerSignal = new Signal1<Paintable>();
			this.lowerSignal.connect(qFusionRenderEngine,
					QFusionRenderEngine.LOWER_SLOTNAME);
		}
		{
			this.setParentSignal = new Signal4<Paintable, Paintable, Integer, Integer>();
			this.setParentSignal.connect(qFusionRenderEngine,
					QFusionRenderEngine.SET_PARENT_SLOTNAME);
		}
		{
			this.setInputFocus = new Signal1<Paintable>();
			this.setInputFocus.connect(qFusionRenderEngine,
					QFusionRenderEngine.SET_INPUTFOCUS_SLOTNAME);
		}
	}

	@Override
	public Paintable getPaintable() {
		return this.paintable;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResourceHandle initPaintPeer(final Paintable parentPaintable,
			final PaintCall<?, ?> paintCall) {

		final ReturnValueWrapper<Long> renderAreaReturnValue = new ReturnValueWrapper<Long>();
		this.createRenderAreaSignal.emit(parentPaintable, this.paintable,
				(QFusionPaintCallBack<? extends QWidget, ?>) paintCall,
				renderAreaReturnValue);

		Long renderAreaId = null;

		try {
			renderAreaId = renderAreaReturnValue.getReturnValue();
		} catch (InterruptedException e) {

			InterruptedPaintResourceInitialization ex = new InterruptedPaintResourceInitialization(
					e);

			LOGGER.error(PAINT_RESOURCE_LOGMESSAGE, ex);

			throw ex;
		}

		return XResourceHandle.valueOf(renderAreaId);
	}

	/**
	 * The <code>QFusionRenderEngine</code> responsible for correctly
	 * implementing all visualization.
	 * 
	 * @return A {@link QFusionRenderEngine}.
	 */
	public QFusionRenderEngine getQtRenderEngine() {
		return this.qFusionRenderEngine;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void paint(final PaintCall<?, ?> paintCall) {
		doBackendWorkd((QFusionPaintCallBack<? extends QWidget, ?>) paintCall);
	}

	private void doBackendWorkd(
			final QFusionPaintCallBack<? extends QWidget, ?> backendWork) {
		this.backendWorkSignal.emit(this.paintable, backendWork);
	}

	@Override
	public void destroy() {
		this.destroySignal.emit(getPaintable());
	}

	@Override
	public void setInputFocus() {
		this.setInputFocus.emit(getPaintable());
	}

	@Override
	public void lower() {
		this.lowerSignal.emit(getPaintable());
	}

	@Override
	public void show() {
		this.showSignal.emit(getPaintable());
	}

	@Override
	public void move(final int x, final int y) {
		this.moveSignal.emit(getPaintable(), Integer.valueOf(x),
				Integer.valueOf(y));
	}

	@Override
	public void moveResize(final int x, final int y, final int width,
			final int height) {
		this.moveResizeSignal.emit(getPaintable(), Integer.valueOf(x),
				Integer.valueOf(y), Integer.valueOf(width),
				Integer.valueOf(height));
	}

	@Override
	public void raise() {
		this.raiseSignal.emit(getPaintable());
	}

	@Override
	public void setParent(final Paintable parent, final int x, final int y) {
		this.setParentSignal.emit(getPaintable(), parent, Integer.valueOf(x),
				Integer.valueOf(y));
	}

	@Override
	public void resize(final int width, final int height) {
		this.resizeSignal.emit(getPaintable(), Integer.valueOf(width),
				Integer.valueOf(height));
	}

	@Override
	public void hide() {
		this.hideSignal.emit(getPaintable());
	}

	@Override
	public void grabKeyboard() {
		doBackendWorkd(new QFusionPaintCallBack<QWidget, QWidget>() {
			@Override
			public QWidget call(final QWidget paintPeer,
					final Paintable paintable) {
				paintPeer.grabKeyboard();
				return paintPeer;
			}
		});
	}

	@Override
	public void grabMouse() {
		doBackendWorkd(new QFusionPaintCallBack<QWidget, QWidget>() {
			@Override
			public QWidget call(final QWidget paintPeer,
					final Paintable paintable) {
				paintPeer.grabMouse();
				return paintPeer;
			}
		});

	}

	@Override
	public void releaseKeyboard() {
		doBackendWorkd(new QFusionPaintCallBack<QWidget, QWidget>() {
			@Override
			public QWidget call(final QWidget paintPeer,
					final Paintable paintable) {
				paintPeer.releaseKeyboard();
				return paintPeer;
			}
		});
	}

	@Override
	public void releaseMouse() {
		doBackendWorkd(new QFusionPaintCallBack<QWidget, QWidget>() {
			@Override
			public QWidget call(final QWidget paintPeer,
					final Paintable paintable) {
				paintPeer.releaseMouse();
				return paintPeer;
			}
		});
	}
}
