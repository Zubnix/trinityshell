package org.trinity.foundation.display.api;

import java.util.Map;

public interface DisplayProtocols {

	Map<String, Object> queryProtocol(	DisplaySurface displaySurface,
										DisplayProtocol displayProtocol);

	Map<String, Object> queryProtocol(	DisplaySurface displaySurface,
										DisplayProtocol displayProtocol,
										Map<String, Object> arguments);
}
