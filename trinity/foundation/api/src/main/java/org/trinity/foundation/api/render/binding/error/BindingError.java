package org.trinity.foundation.api.render.binding.error;

public class BindingError extends Error {
	/*****************************************
	 * 
	 ****************************************/
	private static final long serialVersionUID = 4337530687537263766L;

	public BindingError(final String explanation, final Throwable cause) {
		super(	explanation,
				cause);
	}

	public BindingError(final String explanation) {
		super(explanation);
	}
}
