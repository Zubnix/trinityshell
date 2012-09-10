package org.trinity.render.qtlnf.impl;

import javax.inject.Named;

import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.render.paintengine.qt.api.QJPaintContext;
import org.trinity.shellplugin.widget.api.mvvm.ViewSlot;

import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Named("ShellLabelView")
public class ShellLabelView extends ShellWidgetViewImpl {

	@Override
	protected QWidget createRootVisual(final QWidget parentVisual) {
		return new QLabel(parentVisual);
	}

	@ViewSlot("text")
	protected void setTextInstruction(	final PaintableSurfaceNode paintableSurfaceNode,
										final QJPaintContext paintContext,
										final String text) {
		final QLabel rootVisual = (QLabel) paintContext.getRootVisual();
		rootVisual.setText(text);
	}
}
