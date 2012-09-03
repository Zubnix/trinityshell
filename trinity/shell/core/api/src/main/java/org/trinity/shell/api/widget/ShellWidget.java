package org.trinity.shell.api.widget;

import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.render.api.PaintableRenderNode;
import org.trinity.shell.api.surface.ShellSurface;

public interface ShellWidget extends PaintableRenderNode, DisplayEventSource, ShellSurface {

	void init(ShellWidget paintableParent);

}
