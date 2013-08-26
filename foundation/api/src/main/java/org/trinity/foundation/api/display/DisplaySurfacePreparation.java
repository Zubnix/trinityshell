package org.trinity.foundation.api.display;

import javax.annotation.concurrent.ThreadSafe;

/**
 *
 */
@ThreadSafe
public interface DisplaySurfacePreparation extends AutoCloseable {
	void begin();

	@Override
	void close();
}
