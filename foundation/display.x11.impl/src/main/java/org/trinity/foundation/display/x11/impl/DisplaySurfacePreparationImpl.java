package org.trinity.foundation.display.x11.impl;

import javax.inject.Inject;

import org.trinity.foundation.api.display.DisplaySurfacePreparation;

/**
 *
 */
public class DisplaySurfacePreparationImpl implements DisplaySurfacePreparation {

	private final XEventPump xEventPump;
	private boolean done = false;

	@Inject
	DisplaySurfacePreparationImpl(final XEventPump xEventPump) {
		this.xEventPump = xEventPump;
		xEventPump.stop();
	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public void done() {
		if (!done) {
			this.xEventPump.start();
			done = true;
		}
	}
}
