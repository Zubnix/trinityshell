package org.trinity.display.x11.core.impl;

import org.trinity.display.x11.core.api.XDisplayProtocol;
import org.trinity.foundation.display.api.DisplayRenderArea;

public class XDisplayProtocolImpl implements XDisplayProtocol {

	@Override
	public void requestClose(final DisplayRenderArea displayRenderArea) {
		// final XWindow client = (XWindow) displayRenderArea;
		// TODO send client message
	}
}
