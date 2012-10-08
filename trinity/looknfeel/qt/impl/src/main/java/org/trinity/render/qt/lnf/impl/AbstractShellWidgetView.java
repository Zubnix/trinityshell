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
import org.trinity.shell.api.widget.ShellWidget;
import org.trinity.shell.api.widget.ShellWidgetView;
import org.trinity.shellplugin.widget.api.binding.ViewAttributeSlot;

import com.google.common.io.CharStreams;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QWidget;

public abstract class AbstractShellWidgetView implements ShellWidgetView {

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
	public final Future<Void> createDisplaySurface(	final Painter painter,
													final ShellWidget parent) {
		if (this.painter == null) {
			this.painter = painter;
		} else {
			throw new IllegalStateException("Display surface already created!");
		}

		return invokePaintInstruction(new PaintInstruction<Void, QJPaintContext>() {
			@Override
			public Void call(final QJPaintContext paintContext) {
				try {
					createDisplaySurfaceInstruction(paintContext,
													parent);
					setAllViewAttributes(paintContext);
				} catch (final Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}

	protected void createDisplaySurfaceInstruction(	final QJPaintContext paintContext,
													final ShellWidget parent) {
		QWidget parentVisual = null;
		if (parent != null) {
			parentVisual = paintContext.getVisual(parent);
		}
		final QWidget visual = createVisual(parentVisual);

		try {
			visual.setStyleSheet(getStyleSheet());
			visual.ensurePolished();
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

	protected void setAllViewAttributes(final QJPaintContext paintContext) {
		final PaintableSurfaceNode paintableSurfaceNode = paintContext.getPaintableSurfaceNode();
		// TODO invoke all @viewattributeslots

	}

	protected abstract QWidget createVisual(final QWidget parentVisual);

	protected String getStyleSheet() throws IOException {
		final Class<? extends AbstractShellWidgetView> viewClass = getClass();
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
			return null;
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

	@ViewAttributeSlot("objectName")
	public void setProperty(final QJPaintContext paintContext,
							final String propVal) {
		final QWidget visual = paintContext.getVisual(paintContext.getPaintableSurfaceNode());
		visual.setObjectName(propVal);
	}
}