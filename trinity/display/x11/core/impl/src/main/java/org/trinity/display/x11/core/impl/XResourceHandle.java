package org.trinity.display.x11.core.impl;

import org.trinity.foundation.display.api.ResourceHandle;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class XResourceHandle implements ResourceHandle {

	@Inject
	@Assisted
	private Integer nativeHandle;

	public int getNativeHandle() {
		return this.nativeHandle.intValue();
	}
}
