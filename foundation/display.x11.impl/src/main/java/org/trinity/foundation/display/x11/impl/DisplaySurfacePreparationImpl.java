package org.trinity.foundation.display.x11.impl;

import javax.inject.Inject;

import org.apache.onami.autobind.annotations.Bind;
import org.trinity.foundation.api.display.DisplaySurfacePreparation;

/**
 *
 */
@Bind
public class DisplaySurfacePreparationImpl implements DisplaySurfacePreparation {

	private final XEventPump xEventPump;

	@Inject
	DisplaySurfacePreparationImpl(final XEventPump xEventPump) {
		this.xEventPump = xEventPump;
	}

	@Override
	public void begin() {
		xEventPump.stop();
	}

	@Override
	public void close() {
		this.xEventPump.start();
	}
}
