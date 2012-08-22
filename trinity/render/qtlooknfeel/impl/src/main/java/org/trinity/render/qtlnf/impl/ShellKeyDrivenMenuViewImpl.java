package org.trinity.render.qtlnf.impl;

import java.util.List;
import java.util.concurrent.Future;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.PaintableRenderNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.shell.widget.api.view.ShellKeyDrivenMenuView;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
public class ShellKeyDrivenMenuViewImpl implements ShellKeyDrivenMenuView {

	private Painter painter;

	@Override
	public Future<DisplaySurface> create(final PaintableRenderNode paintableRenderNode) {
		this.painter = paintableRenderNode.getPainter();
	}

	@Override
	public Future<Void> destroy() {

	}

	@Override
	public Future<Void> clear() {

	}

	@Override
	public Future<Void> activate() {

	}

	@Override
	public Future<Void> deactivate() {

	}

	@Override
	public Future<Void> update(	final String input,
								final List<String> filteredChoices,
								final int activeChoiceIdx) {

	}
}