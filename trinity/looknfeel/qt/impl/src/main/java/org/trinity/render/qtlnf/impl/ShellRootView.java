package org.trinity.render.qtlnf.impl;

import javax.inject.Named;

import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.render.paintengine.qt.api.QJPaintContext;

import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Named("shellRootWidgetView")
public class ShellRootView extends ShellWidgetViewImpl {

	@Override
	protected QWidget createRootVisual(final QWidget parentVisual) {
		final QWidget visual = new QFrame(	QApplication.desktop(),
											WindowType.X11BypassWindowManagerHint);
		visual.setStyleSheet("background-color:grey;");
		return visual;
	}

	@Override
	protected void destroyInstruction(	final PaintableSurfaceNode paintableSurfaceNode,
										final QJPaintContext paintContext) {
		// do nothing
	}
}
