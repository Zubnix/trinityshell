package org.trinity.render.qt.lnf.impl;

import javax.inject.Named;

import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Named("ShellWidgetContainerView")
public class ShellWidgetContainerView extends AbstractShellWidgetView {

	@Override
	protected QWidget createVisual(final QWidget parentVisual) {
		final QFrame visual = new QFrame(parentVisual);
		final QLabel lorumIpsum = new QLabel(	"lorum ipsum",
												visual);
		lorumIpsum.show();
		return visual;
	}

}
