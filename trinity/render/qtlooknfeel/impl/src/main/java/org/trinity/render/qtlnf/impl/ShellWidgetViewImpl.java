package org.trinity.render.qtlnf.impl;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

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

	private final AtomicReference<Painter> painterRef = new AtomicReference<Painter>();

	public AtomicReference<Painter> getPainterRef() {
		return this.painterRef;
	}

	@Override
	public Future<Void> createDisplaySurface(final Painter painter) {
		if (!getPainterRef().compareAndSet(	null,
											painter)) {
			return null;
		}

		return getPainterRef().get().instruct(new PaintInstruction<Void, QJPaintContext>() {
			@Override
			public Void call(	final PaintableRenderNode paintableRenderNode,
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
				paintContext.setVisual(visual);
				return null;
			}
		});
	}

	@Override
	public Future<Void> destroy() {
		final Painter painter = getPainterRef().get();
		return painter.instruct(new PaintInstruction<Void, QJPaintContext>() {
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

	@Override
	public Future<DisplaySurface> getDislaySurface() {
		final Painter painter = getPainterRef().get();
		if (painter == null) {
			return null;
		}

		return painter.instruct(new PaintInstruction<DisplaySurface, QJPaintContext>() {
			@Override
			public DisplaySurface call(	final PaintableRenderNode paintableRenderNode,
										final QJPaintContext paintContext) {
				final QWidget visual = paintContext.getVisual();
				final DisplaySurface displaySurface = paintContext.getDisplaySurface(visual);
				return displaySurface;
			}
		});
	}
}