package org.fusion.swing.painter;

import java.awt.Component;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import org.fusion.swing.paintengine.SwingPaintEngine;
 
import org.hydrogen.error.HydrogenPaintInterfaceException;
import org.hydrogen.paintinterface.PaintCall;
import org.hydrogen.paintinterface.Paintable;
import org.hydrogen.paintinterface.Painter;

public class SwingFusionPainter implements Painter {

	public final static class ReturnValueWrapper<T> {
		private final BlockingQueue<T> valueContainer;
		private final int              timeOut;
		private T                      value;

		/**
		 * Create a new <code>ReturnValueWrapper</code> with a default time-out
		 * of 10 seconds for the <code>Thread</code> that will set the return
		 * value.
		 */
		private ReturnValueWrapper() {
			this.valueContainer = new ArrayBlockingQueue<T>(1);
			this.timeOut = 10;
		}

		/**
		 * 
		 * @return
		 * @throws InterruptedException
		 */
		public final T getReturnValue() throws InterruptedException {

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
		public final void setReturnValue(final T returnValue) {

			try {
				if (!this.valueContainer.offer(returnValue,
				                               this.timeOut,
				                               TimeUnit.SECONDS)) {
					throw new RuntimeException(String
					                .format("Time Out.\nCould not set %s for %s",
					                        returnValue,
					                        this));
				}
			} catch (final InterruptedException e) {

				throw new RuntimeException(String.format("Interrupted while waiting for free queue space.\nCould not set %s for %s",
				                                         returnValue,
				                                         this),
				                           e);
			}

		}
	}

	private final Paintable        paintable;
	private final SwingPaintEngine paintEngine;

	public SwingFusionPainter(final SwingPaintEngine paintEngine,
	                          final Paintable paintable) {
		this.paintable = paintable;
		this.paintEngine = paintEngine;
	}

	public SwingPaintEngine getPaintEngine() {
		return this.paintEngine;
	}

	@Override
	public void destroy()    {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					getPaintEngine().destroy(getPaintable());
				} catch (final HydrogenDisplayInterfaceException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void setInputFocus()    {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					getPaintEngine().setInputFocus(getPaintable());
				} catch (final HydrogenDisplayInterfaceException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void lower()    {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					getPaintEngine().lower(getPaintable());
				} catch (final HydrogenDisplayInterfaceException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void show()    {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					getPaintEngine().show(getPaintable());
				} catch (final HydrogenDisplayInterfaceException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void move(final int x,
	                 final int y)    {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					getPaintEngine().move(getPaintable(),
					                      x,
					                      y);
				} catch (final HydrogenDisplayInterfaceException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void moveResize(final int x,
	                       final int y,
	                       final int width,
	                       final int height)
	                   {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					getPaintEngine().moveResize(getPaintable(),
					                            x,
					                            y,
					                            width,
					                            height);
				} catch (final HydrogenDisplayInterfaceException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void raise()    {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					getPaintEngine().raise(getPaintable());
				} catch (final HydrogenDisplayInterfaceException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void setParent(final Paintable parent,
	                      final int x,
	                      final int y)    {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					getPaintEngine().setParent(getPaintable(),
					                           parent,
					                           x,
					                           y);
				} catch (final HydrogenDisplayInterfaceException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void resize(final int width,
	                   final int height)
	                   {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					getPaintEngine().resize(getPaintable(),
					                        width,
					                        height);
				} catch (final HydrogenDisplayInterfaceException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void hide()    {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					getPaintEngine().hide(getPaintable());
				} catch (final HydrogenDisplayInterfaceException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public long createRenderArea(final Paintable parentPaintable,
	                             final PaintCall<?, ?> paintCall)
	                throws HydrogenPaintInterfaceException {
		long windowId;
		try {
			final ReturnValueWrapper<Long> returnValue = new ReturnValueWrapper<Long>();
			SwingUtilities.invokeLater(new Runnable() {
				@SuppressWarnings("unchecked")
				@Override
				public void run() {
					try {
						getPaintEngine().createRenderArea(getPaintable(),
						                                  parentPaintable,
						                                  (PaintCall<Component, ? extends Component>) paintCall,
						                                  returnValue);
					} catch (final HydrogenPaintInterfaceException e) {
						e.printStackTrace();
					}
				}
			});
			windowId = returnValue.getReturnValue().longValue();
		} catch (final InterruptedException e) {
			throw new HydrogenPaintInterfaceException(e);
		}
		return windowId;
	}

	@Override
	public void paint(final PaintCall<?, ?> paintCall)
	                throws HydrogenPaintInterfaceException {
		SwingUtilities.invokeLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				try {
					getPaintEngine().paint(getPaintable(),
					                       (PaintCall<Component, ? extends Component>) paintCall);
				} catch (final HydrogenPaintInterfaceException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void grabKeyboard() throws HydrogenPaintInterfaceException {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					getPaintEngine().grabKeyboard(getPaintable());
				} catch (final HydrogenPaintInterfaceException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void releaseKeyboard() throws HydrogenPaintInterfaceException {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					getPaintEngine().releaseKeyboard(getPaintable());
				} catch (final HydrogenPaintInterfaceException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void grabMouse() throws HydrogenPaintInterfaceException {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					getPaintEngine().grabMouse(getPaintable());
				} catch (final HydrogenPaintInterfaceException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void releaseMouse() throws HydrogenPaintInterfaceException {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					getPaintEngine().releaseMouse(getPaintable());
				} catch (final HydrogenPaintInterfaceException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public Paintable getPaintable() {
		return this.paintable;
	}
}