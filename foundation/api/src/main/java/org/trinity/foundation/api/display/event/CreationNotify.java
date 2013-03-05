package org.trinity.foundation.api.display.event;

import org.trinity.foundation.api.display.DisplaySurface;

public class CreationNotify {
	private final DisplaySurface displaySurface;

	public CreationNotify(DisplaySurface displaySurface) {
		this.displaySurface = displaySurface;
	}

	public DisplaySurface getDisplaySurface() {
		return displaySurface;
	}
}