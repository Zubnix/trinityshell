package org.trinity.render.qtlnf.impl;

import java.util.concurrent.Future;

import javax.inject.Named;

import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableRenderNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.render.paintengine.qt.api.QJPaintContext;

import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Named("shellRootWidgetView")
public class ShellRootView extends ShellWidgetViewImpl {

	@Override
	public Future<Void> createDisplaySurface(final Painter painter) {

		if (!getPainterRef().compareAndSet(	null,
											painter)) {
			return null;
		}

		return painter.instruct(new PaintInstruction<Void, QJPaintContext>() {
			@Override
			public Void call(	final PaintableRenderNode paintableRenderNode,
								final QJPaintContext paintContext) {
				final QWidget visual = new QFrame(	QApplication.desktop(),
													WindowType.X11BypassWindowManagerHint);
				visual.setAttribute(WidgetAttribute.WA_DeleteOnClose,
									true);
				visual.setAttribute(WidgetAttribute.WA_DontCreateNativeAncestors,
									true);
				visual.setStyleSheet("background-color:grey;");
				visual.createWinId();
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
				return null;
			}
		});
	}

}
