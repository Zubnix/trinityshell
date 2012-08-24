package org.trinity.render.qtlnf.impl;

import java.util.concurrent.Future;

import javax.inject.Named;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.display.api.DisplaySurfaceFactory;
import org.trinity.foundation.display.api.DisplaySurfaceHandle;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableRenderNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.render.paintengine.qt.api.QJPaintContext;
import org.trinity.shell.widget.api.view.ShellWidgetView;

import com.google.inject.Inject;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QDesktopWidget;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Named("shellRootWidgetView")
public class ShellRootViewImpl implements ShellWidgetView {

	private Painter painter;
	private final DisplaySurfaceFactory displaySurfaceFactory;

	@Inject
	ShellRootViewImpl(final DisplaySurfaceFactory displaySurfaceFactory) {
		this.displaySurfaceFactory = displaySurfaceFactory;
	}

	@Override
	public Future<DisplaySurface> create(final Painter painter) {
		this.painter = painter;
		return painter.instruct(new PaintInstruction<DisplaySurface, QJPaintContext>() {
			@Override
			public DisplaySurface call(	final PaintableRenderNode paintableRenderNode,
										final QJPaintContext paintContext) {
				final QDesktopWidget visual = QApplication.desktop();
				final DisplaySurfaceHandle displaySurfaceHandle = paintContext.getDisplaySurfaceHandle(visual);
				final DisplaySurface visualDisplaySurface = ShellRootViewImpl.this.displaySurfaceFactory
						.createDisplaySurface(displaySurfaceHandle);
				return visualDisplaySurface;
			}
		});
	}

	@Override
	public Future<Void> destroy() {
		return this.painter.instruct(new PaintInstruction<Void, QJPaintContext>() {
			@Override
			public Void call(	final PaintableRenderNode paintableRenderNode,
								final QJPaintContext paintContext) {
				return null;
			}
		});
	}

}
