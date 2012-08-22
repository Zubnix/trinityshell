package org.trinity.render.qt.impl;

import org.trinity.foundation.display.api.DisplaySurfaceHandle;

import com.trolltech.qt.gui.QWidget;

public class QJDisplaySurfaceHandle implements DisplaySurfaceHandle {

	private final QWidget visual;

	QJDisplaySurfaceHandle(final QWidget visual) {
		this.visual = visual;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof DisplaySurfaceHandle) {
			final DisplaySurfaceHandle otherObj = (DisplaySurfaceHandle) obj;
			return otherObj.getNativeHandle().equals(getNativeHandle());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getNativeHandle().hashCode();
	}

	@Override
	public Integer getNativeHandle() {

		return Integer.valueOf((int) this.visual.effectiveWinId());
	}
}