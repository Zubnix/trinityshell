package org.trinity.shell.widget.api.view;

import java.util.concurrent.Future;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.Painter;

public interface ShellWidgetView {
	Future<DisplaySurface> create(Painter painter);

	Future<Void> destroy();
}