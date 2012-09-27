package org.trinity.render.qt.lnf.impl;

import javax.inject.Named;

import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Named("ShellButtonView")
public class ShellButtonView extends AbstractShellWidgetView {

	@Override
	protected QWidget createVisual(final QWidget parentVisual) {
		return new QFrame(parentVisual);
	}
}