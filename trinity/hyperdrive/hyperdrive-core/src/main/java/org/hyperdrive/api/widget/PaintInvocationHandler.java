/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hyperdrive.api.widget;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.hydrogen.api.paint.PaintCall;
import org.hydrogen.api.paint.Paintable;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
final class PaintInvocationHandler implements InvocationHandler {

	private static final Logger LOGGER = Logger
			.getLogger(PaintInvocationHandler.class);

	private static final String NULL_PAINT_INSTR_ERROR = "Received null PaintInstruction from %s. Ignoring paint call.";
	private static final String NO_VIEW_INSTANCE_SET_WARNING = "Received view update request but no view instance is set for paintable %s. Ignoring.";

	private final Paintable paintable;
	private final Widget.View viewInstance;

	PaintInvocationHandler(final Paintable paintable,
			final Widget.View viewInstance) {
		this.paintable = paintable;
		this.viewInstance = viewInstance;
	}

	@Override
	public Object invoke(final Object proxy, final Method method,
			final Object[] args) throws Throwable {
		if (this.viewInstance == null) {
			PaintInvocationHandler.LOGGER.warn(String.format(
					PaintInvocationHandler.NO_VIEW_INSTANCE_SET_WARNING,
					this.paintable));
			return null;
		}

		// delegate paint instruction to painter and return result
		final Class<?> returnType = method.getReturnType();
		if (PaintInstruction.class.isAssignableFrom(returnType)) {
			final PaintInstruction<?> paintInstruction = (PaintInstruction<?>) method
					.invoke(this.viewInstance, args);

			if (paintInstruction == null) {
				PaintInvocationHandler.LOGGER.error(String.format(
						PaintInvocationHandler.NULL_PAINT_INSTR_ERROR,
						this.viewInstance));
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