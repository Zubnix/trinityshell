package org.trinity.shell.widget.api.view;

import org.trinity.foundation.display.api.DisplayRenderArea;
import org.trinity.foundation.render.api.PaintableRenderNode;

public interface ShellWidgetView {
	DisplayRenderArea create(PaintableRenderNode paintableRenderNode);

	void destroy();
}