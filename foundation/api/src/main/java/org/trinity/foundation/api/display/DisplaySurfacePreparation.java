package org.trinity.foundation.api.display;

import javax.annotation.concurrent.ThreadSafe;

/**
 *
 */
@ThreadSafe
public interface DisplaySurfacePreparation {
	boolean isDone();

    void done();
}
