package org.trinity.foundation.api.display;

import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.Size;

@ExecutionContext(DisplayExecutor.class)
public interface Screen {

	Size getSize();
}
