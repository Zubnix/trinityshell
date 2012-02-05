package org.hypercube.error;

import org.hyperdrive.widget.View;

public class InaccessibleWidgetViewClass extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6590305001332039337L;

	public static final String MESSAGE = "Unable to instantiate widget view from class %s. Did you provide a default constructor?";
	
	public InaccessibleWidgetViewClass(Class<? extends View> viewClass, Exception e) {
		super(String.format(MESSAGE, viewClass.getCanonicalName()), e);
	}
}
