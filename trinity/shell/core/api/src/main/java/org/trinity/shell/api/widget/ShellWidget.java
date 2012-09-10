package org.trinity.shell.api.widget;

import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.shell.api.surface.ShellSurface;

public interface ShellWidget extends PaintableSurfaceNode, DisplayEventSource, ShellSurface {

	void init(ShellWidget paintableParent);

}
