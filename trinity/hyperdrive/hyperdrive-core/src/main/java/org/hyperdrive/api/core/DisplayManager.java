package org.hyperdrive.api.core;

import org.hydrogen.api.config.DisplayConfiguration;

public interface DisplayManager {
	ManagedDisplay getNewManagedDisplay(
			DisplayConfiguration displayConfiguration);
}
