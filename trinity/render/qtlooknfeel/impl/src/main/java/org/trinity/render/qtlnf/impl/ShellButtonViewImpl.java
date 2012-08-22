package org.trinity.render.qtlnf.impl;

import java.util.concurrent.Future;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.PaintableRenderNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.shell.widget.api.view.ShellButtonView;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
public class ShellButtonViewImpl implements ShellButtonView {

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
	public Future<Void> pressed() {
		// TODO Auto-generated method stub

	}

	@Override
	public Future<Void> released() {
		// TODO Auto-generated method stub

	}

}
