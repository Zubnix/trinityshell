package org.trinity.render.qt.lnf.impl;

import javax.inject.Named;

import org.trinity.render.qt.api.QJPaintContext;
import org.trinity.shellplugin.widget.api.binding.ViewAttributeSlot;

import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Named("ShellLabelView")
public class ShellLabelView extends ShellWidgetViewImpl {

	@Override
	protected QWidget createVisual(final QWidget parentVisual) {
		return new QLabel(parentVisual);
	}

	@ViewAttributeSlot("text")
	protected void handleTextChanged(	final QJPaintContext paintContext,
										final String text) {
		final QLabel visual = (QLabel) paintContext.getVisual(paintContext.getPaintableSurfaceNode());
		visual.setText(text);
	}

	@Override
	@ViewAttributeSlot("name")
	public void setName(	final QJPaintContext paintContext,
									final String name) {
		paintContext.getVisual(paintContext.getPaintableSurfaceNode()).setObjectName(name);
	}
}
