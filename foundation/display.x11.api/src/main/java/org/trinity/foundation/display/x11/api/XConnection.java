package org.trinity.foundation.display.x11.api;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.render.bindkey.RenderExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;

@NotThreadSafe
@ExecutionContext(DisplayExecutor.class)
public interface XConnection {
	SWIGTYPE_p_xcb_connection_t getConnectionReference();

	void open(	@Nonnull String displayName,
				@Nonnegative int screen);

	void close();
}
