package org.hyperdrive.widget;

import java.util.concurrent.Future;

import org.hydrogen.api.paint.PaintCall;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public final class PaintInstruction<T> {
	private PaintCall<T, ?> paintCall;
	private Future<T> paintResult;

	public PaintInstruction(final PaintCall<T, ?> paintCall) {
		this.paintCall = paintCall;
	}

	/**
	 * Reserved for view proxy implementation.
	 * 
	 * @param paintResult
	 */
	protected PaintInstruction(final PaintCall<T, ?> paintCall,
			final Future<T> paintResult) {
		this(paintCall);
		this.paintResult = paintResult;
	}

	public PaintCall<T, ?> getPaintCall() {
		return this.paintCall;
	}

	public Future<T> getPaintResult() {
		return this.paintResult;
	}
}
