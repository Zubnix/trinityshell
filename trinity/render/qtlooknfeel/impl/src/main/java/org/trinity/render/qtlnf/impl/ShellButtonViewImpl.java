package org.trinity.render.qtlnf.impl;

import java.util.concurrent.Future;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.display.api.DisplaySurfaceFactory;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableRenderNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.render.paintengine.qt.api.QJPaintContext;
import org.trinity.shell.widget.api.view.ShellButtonView;

import com.google.inject.Inject;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
public class ShellButtonViewImpl implements ShellButtonView {

	private final DisplaySurfaceFactory displaySurfaceFactory;

	private Painter painter;

	@Inject
	ShellButtonViewImpl(final DisplaySurfaceFactory displaySurfaceFactory) {
		this.displaySurfaceFactory = displaySurfaceFactory;
	}

	@Override
	public Future<DisplaySurface> create(final Painter painter) {
		this.painter = painter;
		return this.painter
				.instruct(new PaintInstruction<DisplaySurface, QJPaintContext>() {
					@Override
					public DisplaySurface call(	final PaintableRenderNode paintableRenderNode,
												final QJPaintContext paintContext) {
					}
				});
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
