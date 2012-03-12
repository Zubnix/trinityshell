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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.fusion.qt.paintengine.QFusionRenderEngine;
import org.hydrogen.api.geometry.Coordinates;
import org.hydrogen.api.paint.PaintCall;
import org.hydrogen.api.paint.PaintContext;
import org.hydrogen.api.paint.Paintable;
import org.hydrogen.api.paint.PaintableRef;
import org.hydrogen.api.paint.Painter;
import org.hydrogen.geometry.BaseCoordinates;
import org.hydrogen.paint.BasePaintableRef;

import com.trolltech.qt.core.QPoint;
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
public class QFusionPainter implements Painter {

	private static final QFusionPaintCall<Void, QWidget> HIDE_CALL = new QFusionPaintCall<Void, QWidget>() {
		@Override
		public Void call(final PaintContext<QWidget> paintContext) {
			paintContext.getPaintPeer().hide();
			return null;
		}
	};

	private static final QFusionPaintCall<Void, QWidget> LOWER_CALL = new QFusionPaintCall<Void, QWidget>() {
		@Override
		public Void call(final PaintContext<QWidget> paintContext) {
			paintContext.getPaintPeer().lower();
			return null;
		}
	};

	private static final QFusionPaintCall<Void, QWidget> RAISE_CALL = new QFusionPaintCall<Void, QWidget>() {
		@Override
		public Void call(final PaintContext<QWidget> paintContext) {
			paintContext.getPaintPeer().raise();
			return null;
		}
	};

	private static final QFusionPaintCall<Void, QWidget> SHOW_CALL = new QFusionPaintCall<Void, QWidget>() {
		@Override
		public Void call(final PaintContext<QWidget> paintContext) {
			paintContext.getPaintPeer().show();
			return null;
		}
	};

	private static final QFusionPaintCall<Void, QWidget> GRAB_KEYBOARD_CALL = new QFusionPaintCall<Void, QWidget>() {
		@Override
		public Void call(final PaintContext<QWidget> paintContext) {
			paintContext.getPaintPeer().grabKeyboard();
			return null;
		}
	};

	private static final QFusionPaintCall<Void, QWidget> RELEASE_KEYBOARD_CALL = new QFusionPaintCall<Void, QWidget>() {
		@Override
		public Void call(final PaintContext<QWidget> paintContext) {
			paintContext.getPaintPeer().releaseKeyboard();
			return null;
		}
	};

	private static final QFusionPaintCall<Void, QWidget> GRAB_MOUSE_CALL = new QFusionPaintCall<Void, QWidget>() {
		@Override
		public Void call(final PaintContext<QWidget> paintContext) {
			paintContext.getPaintPeer().grabMouse();
			return null;
		}
	};

	private static final QFusionPaintCall<Void, QWidget> RELEASE_MOUSE_CALL = new QFusionPaintCall<Void, QWidget>() {
		@Override
		public Void call(final PaintContext<QWidget> paintContext) {
			paintContext.getPaintPeer().releaseMouse();
			return null;
		}
	};

	private static final QFusionPaintCall<Void, QWidget> DESTROY_CALL = new QFusionPaintCall<Void, QWidget>() {
		@Override
		public Void call(final PaintContext<QWidget> paintContext) {
			paintContext.getPaintPeer().close();
			return null;
		}
	};

	private static final QFusionPaintCall<Void, QWidget> SET_INPUT_CALL = new QFusionPaintCall<Void, QWidget>() {
		@Override
		public Void call(final PaintContext<QWidget> paintContext) {
			paintContext.getPaintPeer().setFocus();
			return null;
		}
	};

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
		this.qFusionRenderEngine = qFusionRenderEngine;
		this.paintable = paintable;
	}

	@Override
	public Paintable getPaintable() {
		return this.paintable;
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

	private <T> Future<T> doBackendWork(final QFusionPaintCall<T, ?> backendWork) {
		return getQtRenderEngine().invoke(getPaintable(), backendWork);
	}

	@Override
	public void destroy() {
		doBackendWork(QFusionPainter.DESTROY_CALL);
	}

	@Override
	public void setInputFocus() {
		doBackendWork(QFusionPainter.SET_INPUT_CALL);
	}

	@Override
	public void lower() {
		doBackendWork(QFusionPainter.LOWER_CALL);
	}

	@Override
	public void show() {
		doBackendWork(QFusionPainter.SHOW_CALL);
	}

	@Override
	public void move(final int x, final int y) {
		doBackendWork(new QFusionPaintCall<Void, QWidget>() {
			@Override
			public Void call(final PaintContext<QWidget> paintContext) {
				paintContext.getPaintPeer().move(x, y);
				return null;
			}
		});
	}

	@Override
	public void moveResize(final int x, final int y, final int width,
			final int height) {
		doBackendWork(new QFusionPaintCall<Void, QWidget>() {
			@Override
			public Void call(final PaintContext<QWidget> paintContext) {
				paintContext.getPaintPeer().setGeometry(x, y, width, height);
				return null;
			}
		});
	}

	@Override
	public void raise() {
		doBackendWork(QFusionPainter.RAISE_CALL);
	}

	@Override
	public void setParent(final Paintable parent, final int x, final int y) {
		doBackendWork(new QFusionPaintCall<Void, QWidget>() {
			@Override
			public Void call(final PaintContext<QWidget> paintContext) {
				final PaintableRef parentPaintableRef = new BasePaintableRef(
						parent);
				final QWidget parentPaintpeer = (QWidget) paintContext
						.queryPaintPeer(parentPaintableRef);
				paintContext.getPaintPeer().setParent(parentPaintpeer);
				return null;
			}
		});
	}

	@Override
	public void resize(final int width, final int height) {
		doBackendWork(new QFusionPaintCall<Void, QWidget>() {
			@Override
			public Void call(final PaintContext<QWidget> paintContext) {
				paintContext.getPaintPeer().resize(width, height);
				return null;
			}
		});
	}

	@Override
	public void hide() {
		doBackendWork(QFusionPainter.HIDE_CALL);
	}

	@Override
	public void grabKeyboard() {
		doBackendWork(QFusionPainter.GRAB_KEYBOARD_CALL);
	}

	@Override
	public void grabMouse() {
		doBackendWork(QFusionPainter.GRAB_MOUSE_CALL);
	}

	@Override
	public void releaseKeyboard() {
		doBackendWork(QFusionPainter.RELEASE_KEYBOARD_CALL);
	}

	@Override
	public void releaseMouse() {
		doBackendWork(QFusionPainter.RELEASE_MOUSE_CALL);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Future<T> paint(final PaintCall<T, ?> paintCall) {
		return doBackendWork((QFusionPaintCall<T, ? extends QWidget>) paintCall);
	}

	@Override
	public Coordinates translateCoordinates(final Paintable source,
			final int sourceX, final int sourceY) {
		final Future<Coordinates> task = doBackendWork(new QFusionPaintCall<Coordinates, QWidget>() {
			@Override
			public Coordinates call(final PaintContext<QWidget> paintContext) {
				final PaintableRef sourcePaintableRef = new BasePaintableRef(
						source);

				final QWidget sourcePaintPeer = (QWidget) paintContext
						.queryPaintPeer(sourcePaintableRef);
				final QWidget targetPaintPeer = paintContext.getPaintPeer();

				final QPoint translatedPoint = sourcePaintPeer.mapTo(
						targetPaintPeer, new QPoint(sourceX, sourceY));
				final Coordinates coordinates = new BaseCoordinates(
						translatedPoint.x(), translatedPoint.y());
				return coordinates;

			}
		});

		Coordinates result = null;
		try {
			result = task.get();
		} catch (final InterruptedException e) {
			// TODO throw exception+log
			e.printStackTrace();
		} catch (final ExecutionException e) {
			// TODO throw exception+log
			e.printStackTrace();
		}
		return result;
	}
}
