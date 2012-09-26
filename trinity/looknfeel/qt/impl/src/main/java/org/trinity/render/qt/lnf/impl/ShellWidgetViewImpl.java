package org.trinity.render.qt.lnf.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Future;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.render.qt.api.QJPaintContext;
import org.trinity.shell.api.widget.ShellWidgetView;

import com.google.common.io.CharStreams;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QFrame;
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
			public Void call(final QJPaintContext paintContext) {
				createDisplaySurfaceInstruction(paintContext);
				return null;
			}
		});
	}

	protected void createDisplaySurfaceInstruction(final QJPaintContext paintContext) {
		final PaintableSurfaceNode paintableSurfaceNode = paintContext.getPaintableSurfaceNode();
		final PaintableSurfaceNode parentSurfaceNode = paintableSurfaceNode.getParentPaintableSurface();
		QWidget parentVisual = null;
		if (parentSurfaceNode != null) {
			parentVisual = paintContext.getVisual(parentSurfaceNode);
		}
		final QWidget visual = createVisual(parentVisual);

		try {
			visual.setObjectName(getClass().getSimpleName());
			visual.setStyleSheet(getStyleSheet());
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		visual.setWindowFlags(WindowType.X11BypassWindowManagerHint);
		visual.setAttribute(WidgetAttribute.WA_DeleteOnClose,
							true);
		visual.setAttribute(WidgetAttribute.WA_DontCreateNativeAncestors,
							true);

		paintContext.syncVisualGeometryToSurfaceNode(visual);
		paintContext.setVisual(visual);
	}

	protected QWidget createVisual(final QWidget parentVisual) {
		final QWidget visual = new QFrame(parentVisual);
		return visual;
	}

	protected String getStyleSheet() throws IOException {
		final Class<? extends ShellWidgetViewImpl> viewClass = getClass();
		final String className = viewClass.getName();
		final InputStream in = viewClass.getClassLoader().getResourceAsStream(className + ".qss");
		final InputStreamReader inReader = new InputStreamReader(	in,
																	"UTF-8");
		String styleSheet = "";
		try {
			styleSheet = CharStreams.toString(inReader);
		} finally {
			inReader.close();
		}
		return styleSheet;
	}

	@Override
	public final Future<Void> destroy() {
		return invokePaintInstruction(new PaintInstruction<Void, QJPaintContext>() {
			@Override
			public Void call(final QJPaintContext paintContext) {
				destroyInstruction(paintContext);
				return null;
			}
		});
	}

	protected void destroyInstruction(final QJPaintContext paintContext) {
		final QWidget visual = paintContext.getVisual(paintContext.getPaintableSurfaceNode());
		visual.close();
		paintContext.disposeVisual();
	}

	@Override
	public final Future<DisplaySurface> getDislaySurface() {
		final Painter painter = getPainter();
		if (this.painter == null) {
			throw new IllegalStateException("Display surface not created!");
		}
		return painter.instruct(new PaintInstruction<DisplaySurface, QJPaintContext>() {
			@Override
			public DisplaySurface call(final QJPaintContext paintContext) {
				return getDisplaySurfaceInstruction(paintContext);
			}
		});
	}

	protected DisplaySurface getDisplaySurfaceInstruction(final QJPaintContext paintContext) {
		final QWidget visual = paintContext.getVisual(paintContext.getPaintableSurfaceNode());
		final DisplaySurface displaySurface = paintContext.getDisplaySurface(visual);
		return displaySurface;
	}
}