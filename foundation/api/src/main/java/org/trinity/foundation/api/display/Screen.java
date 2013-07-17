package org.trinity.foundation.api.display;

import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.Size;

import javax.annotation.concurrent.ThreadSafe;

@ExecutionContext(DisplayExecutor.class)
@ThreadSafe
public interface Screen {

	Size getSize();
}
