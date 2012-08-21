package org.trinity.display.x11.core.impl;

import org.trinity.foundation.display.api.DisplaySurfaceHandle;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class XWindowHandle implements DisplaySurfaceHandle {

	private final Integer nativeHandle;

	@Inject
	XWindowHandle(@Assisted final Object nativeHandle) {
		if (nativeHandle instanceof Integer) {
			this.nativeHandle = (Integer) nativeHandle;
		} else {
			throw new Error("Can only handle handle native X window handle of type 'Integer'. Got native handle of type: "
					+ nativeHandle.getClass().getName());
		}
	}

	@Override
	public Integer getNativeHandle() {
		return this.nativeHandle;
	}

	@Override
	public int hashCode() {
		return getNativeHandle().hashCode();
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