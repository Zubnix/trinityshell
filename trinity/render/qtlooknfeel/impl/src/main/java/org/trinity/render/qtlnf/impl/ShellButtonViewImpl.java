package org.trinity.render.qtlnf.impl;

import java.util.concurrent.Future;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableRenderNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.render.paintengine.qt.api.QJPaintContext;

import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
public class ShellButtonViewImpl extends ShellWidgetViewImpl {

	private Painter painter;

	@Override
	public Future<DisplaySurface> create(final Painter painter) {
		this.painter = painter;
		return this.painter.instruct(new PaintInstruction<DisplaySurface, QJPaintContext>() {
			@Override
			public DisplaySurface call(	final PaintableRenderNode paintableRenderNode,
										final QJPaintContext paintContext) {
				final QWidget parent = paintContext.queryVisual(paintableRenderNode.getParentPaintableRenderNode());
				final QPushButton visual = new QPushButton(parent);
				visual.setWindowFlags(WindowType.X11BypassWindowManagerHint);
				visual.setAttribute(WidgetAttribute.WA_DeleteOnClose,
									true);
				visual.setAttribute(WidgetAttribute.WA_DontCreateNativeAncestors,
									true);

				final DisplaySurface visualDisplaySurface = paintContext.getDisplaySurface(visual);
				return visualDisplaySurface;
			}
		});
	}
}
