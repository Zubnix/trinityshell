package org.trinity.render.qtlnf.impl;

import java.util.List;
import java.util.concurrent.Future;

import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableRenderNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.render.paintengine.qt.api.QJPaintContext;
import org.trinity.shell.widget.api.view.ShellKeyDrivenMenuView;

import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
public class ShellKeyDrivenMenuViewImpl extends ShellWidgetViewImpl implements ShellKeyDrivenMenuView {

	private Painter painter;

	@Override
	public Future<Void> createDisplaySurface(final Painter painter) {
		this.painter = painter;
		return painter.instruct(new PaintInstruction<Void, QJPaintContext>() {
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

				return null;
			}
		});
	}

	@Override
	public Future<Void> clear() {
		return this.painter.instruct(new PaintInstruction<Void, QJPaintContext>() {
			@Override
			public Void call(	final PaintableRenderNode paintableRenderNode,
								final QJPaintContext paintContext) {
				return null;
			}
		});
	}

	@Override
	public Future<Void> activate() {
		return this.painter.instruct(new PaintInstruction<Void, QJPaintContext>() {
			@Override
			public Void call(	final PaintableRenderNode paintableRenderNode,
								final QJPaintContext paintContext) {
				return null;
			}
		});
	}

	@Override
	public Future<Void> deactivate() {
		return this.painter.instruct(new PaintInstruction<Void, QJPaintContext>() {
			@Override
			public Void call(	final PaintableRenderNode paintableRenderNode,
								final QJPaintContext paintContext) {
				return null;
			}
		});
	}

	@Override
	public Future<Void> update(	final String input,
								final List<String> filteredChoices,
								final int activeChoiceIdx) {
		return this.painter.instruct(new PaintInstruction<Void, QJPaintContext>() {
			@Override
			public Void call(	final PaintableRenderNode paintableRenderNode,
								final QJPaintContext paintContext) {
				return null;
			}
		});
	}
}