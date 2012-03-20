package org.hyperdrive.api.core.event;

import org.hyperdrive.api.core.RenderArea;

public interface ClientCreatedHandler {
	void handleCreatedClient(RenderArea client);
}
