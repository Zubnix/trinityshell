package org.trinity.display.x11.core.impl;

import java.util.Map;

import org.trinity.foundation.display.api.DisplayProtocol;

public interface XDisplayProtocolHandler {

	Map<String, Object> handleDipslayProtocol(	XWindow xWindow,
												DisplayProtocol displayProtocol,
												Map<String, Object> arguments);

	DisplayProtocol getDisplayProtocol();
}
