package org.trinity.shell.widget.api.view;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.PaintableRenderNode;

public interface ShellWidgetView {
	DisplaySurface create(PaintableRenderNode paintableRenderNode);

	void destroy();
}