package org.trinity.shell.api.widget;

import java.util.concurrent.Future;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.Painter;

public interface ShellWidgetView {
	Future<Void> createDisplaySurface(	Painter painter,
										ShellWidget closestParentWidget);

	Future<DisplaySurface> getDislaySurface();

	Future<Void> destroy();
}