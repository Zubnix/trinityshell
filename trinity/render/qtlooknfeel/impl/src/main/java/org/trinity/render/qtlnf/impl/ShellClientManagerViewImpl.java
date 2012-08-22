package org.trinity.render.qtlnf.impl;

import java.util.concurrent.Future;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.PaintableRenderNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.shell.core.api.ShellSurface;
import org.trinity.shell.widget.api.view.ShellClientManagerView;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
public class ShellClientManagerViewImpl implements ShellClientManagerView {

	private Painter painter;

	@Override
	public Future<DisplaySurface> create(final PaintableRenderNode paintableRenderNode) {
		this.painter = paintableRenderNode.getPainter();
	}

	@Override
	public Future<Void> destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public Future<Void> addClient(final ShellSurface client) {
		// TODO Auto-generated method stub

	}

	@Override
	public Future<Void> removeClient(final ShellSurface client) {
		// TODO Auto-generated method stub

	}

}
