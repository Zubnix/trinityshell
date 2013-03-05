package org.trinity.foundation.api.display.event;

import javax.annotation.concurrent.Immutable;

import org.trinity.foundation.api.display.DisplaySurface;

@Immutable
public class CreationNotify extends DisplayEvent {
	private final DisplaySurface displaySurface;

	public CreationNotify(final DisplaySurface displaySurface) {
		this.displaySurface = displaySurface;
	}

	public DisplaySurface getDisplaySurface() {
		return this.displaySurface;
	}
}