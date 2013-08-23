package org.trinity.foundation.display.x11.impl;

import javax.inject.Inject;

import org.trinity.foundation.api.display.DisplaySurfacePreparation;

/**
 *
 */
public class DisplaySurfacePreparationImpl implements DisplaySurfacePreparation {

	private final XEventPump xEventPump;

	@Inject
	DisplaySurfacePreparationImpl(final XEventPump xEventPump) {
		this.xEventPump = xEventPump;
		xEventPump.stop();
	}

	@Override
	public void done() {
		this.xEventPump.start();
	}
}
