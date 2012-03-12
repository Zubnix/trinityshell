package org.hyperdrive.widget;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Future;

import org.hydrogen.api.paint.PaintCall;
import org.hydrogen.api.paint.Paintable;
import org.hyperdrive.api.widget.PaintInstruction;
import org.hyperdrive.api.widget.Widget;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class PaintInvocationHandler implements InvocationHandler {

	private final Paintable paintable;
	private final Widget.View viewInstance;

	public PaintInvocationHandler(final Paintable paintable,
			final Widget.View viewInstance) {
		this.paintable = paintable;
		this.viewInstance = viewInstance;
	}

	@Override
	public Object invoke(final Object proxy, final Method method,
			final Object[] args) throws Throwable {
		if (this.viewInstance == null) {
			ViewBinder.LOGGER.warn(String.format(
					ViewBinder.NO_VIEW_INSTANCE_SET_WARNING, this.paintable));
			return null;
		}

		// delegate paint instruction to painter and return result
		final Class<?> returnType = method.getReturnType();
		if (PaintInstruction.class.isAssignableFrom(returnType)) {
			final PaintInstruction<?> paintInstruction = (PaintInstruction<?>) method
					.invoke(this.viewInstance, args);

			if (paintInstruction == null) {
				ViewBinder.LOGGER.error(String.format(
						ViewBinder.NULL_PAINT_INSTR_ERROR, this.viewInstance));
				return null;
			}

			final PaintCall<?, ?> paintCall = paintInstruction.getPaintCall();

			final Future<?> result = this.paintable.getPainter().paint(
					paintCall);

			@SuppressWarnings({ "unchecked", "rawtypes" })
			final PaintInstruction<?> completedPaintInstruction = new PaintInstruction(
					paintCall, result);
			return completedPaintInstruction;
		}

		// not a paint instruction, simply return it.
		return method.invoke(this.viewInstance, args);
	}
}