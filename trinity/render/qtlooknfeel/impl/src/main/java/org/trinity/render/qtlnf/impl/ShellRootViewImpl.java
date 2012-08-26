package org.trinity.render.qtlnf.impl;

import java.util.concurrent.Future;

import javax.inject.Named;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableRenderNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.render.paintengine.qt.api.QJPaintContext;
import org.trinity.shell.widget.api.view.ShellWidgetView;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QDesktopWidget;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Named("shellRootWidgetView")
public class ShellRootViewImpl implements ShellWidgetView {

	private Painter painter;

	@Override
	public Future<DisplaySurface> create(final Painter painter) {
		this.painter = painter;
		return painter.instruct(new PaintInstruction<DisplaySurface, QJPaintContext>() {
			@Override
			public DisplaySurface call(	final PaintableRenderNode paintableRenderNode,
										final QJPaintContext paintContext) {
				final QDesktopWidget visual = QApplication.desktop();
				visual.setFixedSize(visual.frameSize());
				final DisplaySurface visualDisplaySurface = paintContext.getDisplaySurface(visual);
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
