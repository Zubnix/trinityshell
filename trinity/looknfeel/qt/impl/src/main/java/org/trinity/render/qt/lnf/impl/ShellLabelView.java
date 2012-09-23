package org.trinity.render.qt.lnf.impl;

import javax.inject.Named;

import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.render.qt.api.QJPaintContext;
import org.trinity.shellplugin.widget.api.binding.ViewAttributeSlot;

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

	@ViewAttributeSlot("text")
	protected void handleTextChanged(	final PaintableSurfaceNode paintableSurfaceNode,
										final QJPaintContext paintContext,
										final String text) {
		final QLabel visual = (QLabel) paintContext.getVisual();
		visual.setText(text);
	}
}
