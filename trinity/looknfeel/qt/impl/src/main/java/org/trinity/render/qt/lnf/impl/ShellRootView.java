package org.trinity.render.qt.lnf.impl;

import javax.inject.Named;

import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Named("ShellRootView")
public class ShellRootView extends ShellWidgetViewImpl {

	@Override
	protected QWidget createVisual(final QWidget parentVisual) {
		final QWidget visual = new QFrame(	QApplication.desktop(),
											WindowType.X11BypassWindowManagerHint);
		visual.setStyleSheet("background-color:grey;");
		return visual;
	}
}
