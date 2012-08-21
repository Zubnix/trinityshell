package org.trinity.display.x11.core.impl;

import org.trinity.foundation.display.api.DisplaySurfaceHandle;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class XWindowHandle implements DisplaySurfaceHandle {

	private final int nativeHandle;

	@Inject
	XWindowHandle(@Assisted final int nativeHandle) {
		this.nativeHandle = nativeHandle;
	}

	public int getNativeHandle() {
		return this.nativeHandle;
	}

	@Override
	public int hashCode() {
		return getNativeHandle();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof XWindowHandle) {
			final XWindowHandle otherXResourceHandle = (XWindowHandle) obj;
			return otherXResourceHandle.getNativeHandle() == getNativeHandle();
		}
		return false;
	}
}