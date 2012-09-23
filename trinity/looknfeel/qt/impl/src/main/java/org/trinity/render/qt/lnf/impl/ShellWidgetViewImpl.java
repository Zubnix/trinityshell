package org.trinity.render.qt.lnf.impl;

import java.util.concurrent.Future;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.render.qt.api.QJPaintContext;
import org.trinity.shell.api.widget.ShellWidgetView;

import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
public class ShellWidgetViewImpl implements ShellWidgetView {

	private Painter painter;

	protected Painter getPainter() {
		return this.painter;
	}

	protected <R> Future<R> invokePaintInstruction(final PaintInstruction<R, QJPaintContext> paintInstruction) {
		final Painter painter = getPainter();
		if (this.painter == null) {
			throw new IllegalStateException("Display surface not created!");
		}
		return painter.instruct(paintInstruction);
	}

	@Override
	public final Future<Void> createDisplaySurface(final Painter painter) {
		if (this.painter == null) {
			this.painter = painter;
		} else {
			throw new IllegalStateException("Display surface already created!");
		}

		return invokePaintInstruction(new PaintInstruction<Void, QJPaintContext>() {
			@Override
			public Void call(	final PaintableSurfaceNode paintableSurfaceNode,
								final QJPaintContext paintContext) {
				createDisplaySurfaceInstruction(paintableSurfaceNode,
												paintContext);
				return null;
			}
		});
	}

	protected void createDisplaySurfaceInstruction(	final PaintableSurfaceNode paintableSurfaceNode,
													final QJPaintContext paintContext) {
		final PaintableSurfaceNode parentSurfaceNode = paintableSurfaceNode.getParentPaintableSurface();
		QWidget parentVisual = null;
		if (parentSurfaceNode != null) {
			parentVisual = paintContext.queryVisual(parentSurfaceNode);
		}
		final QWidget visual = createRootVisual(parentVisual);
		visual.setWindowFlags(WindowType.X11BypassWindowManagerHint);
		visual.setAttribute(WidgetAttribute.WA_DeleteOnClose,
							true);
		visual.setAttribute(WidgetAttribute.WA_DontCreateNativeAncestors,
							true);

		paintContext.syncVisualGeometryToSurfaceNode(	visual,
														paintableSurfaceNode);
		paintContext.setVisual(visual);
	}

	protected QWidget createRootVisual(final QWidget parentVisual) {
		final QWidget visual = new QWidget(parentVisual);
		return visual;
	}

	@Override
	public final Future<Void> destroy() {
		return invokePaintInstruction(new PaintInstruction<Void, QJPaintContext>() {
			@Override
			public Void call(	final PaintableSurfaceNode paintableSurfaceNode,
								final QJPaintContext paintContext) {
				destroyInstruction(	paintableSurfaceNode,
									paintContext);
				return null;
			}
		});
	}

	protected void destroyInstruction(	final PaintableSurfaceNode paintableSurfaceNode,
										final QJPaintContext paintContext) {
		final QWidget visual = paintContext.getVisual();
		visual.close();
		paintContext.evictVisual();
	}

	@Override
	public final Future<DisplaySurface> getDislaySurface() {
		final Painter painter = getPainter();
		if (this.painter == null) {
			throw new IllegalStateException("Display surface not created!");
		}
		return painter.instruct(new PaintInstruction<DisplaySurface, QJPaintContext>() {
			@Override
			public DisplaySurface call(	final PaintableSurfaceNode paintableSurfaceNode,
										final QJPaintContext paintContext) {
				return getDisplaySurfaceInstruction(paintableSurfaceNode,
													paintContext);
			}
		});
	}

	protected DisplaySurface getDisplaySurfaceInstruction(	final PaintableSurfaceNode paintableSurfaceNode,
															final QJPaintContext paintContext) {
		final QWidget visual = paintContext.getVisual();
		final DisplaySurface displaySurface = paintContext.getDisplaySurface(visual);
		return displaySurface;
	}
}