package org.trinity.render.qtlnf.impl;

import java.util.concurrent.Future;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableRenderNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.render.paintengine.qt.api.QJPaintContext;
import org.trinity.shell.widget.api.view.ShellWidgetView;

import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
public class ShellWidgetViewImpl implements ShellWidgetView {

	private Painter painter;

	@Override
	public Future<DisplaySurface> create(final Painter painter) {
		this.painter = painter;

		return this.painter.instruct(new PaintInstruction<DisplaySurface, QJPaintContext>() {
			@Override
			public DisplaySurface call(	final PaintableRenderNode paintableRenderNode,
										final QJPaintContext paintContext) {

				final QWidget parentVisual = paintContext.queryVisual(paintableRenderNode
						.getParentPaintableRenderNode());
				final QWidget visual = new QWidget(parentVisual);
				visual.setWindowFlags(WindowType.X11BypassWindowManagerHint);
				visual.setAttribute(WidgetAttribute.WA_DeleteOnClose,
									true);
				visual.setAttribute(WidgetAttribute.WA_DontCreateNativeAncestors,
									true);

				paintContext.syncVisualGeometryToNode(	visual,
														paintableRenderNode);

				final DisplaySurface displaySurface = paintContext.getDisplaySurface(visual);
				return displaySurface;
			}
		});
	}

	@Override
	public Future<Void> destroy() {
		return this.painter.instruct(new PaintInstruction<Void, QJPaintContext>() {
			@Override
			public Void call(	final PaintableRenderNode paintableRenderNode,
								final QJPaintContext paintContext) {
				final QWidget visual = paintContext.getVisual();
				visual.close();
				paintContext.evictVisual();
				return null;
			}
		});
	}
}