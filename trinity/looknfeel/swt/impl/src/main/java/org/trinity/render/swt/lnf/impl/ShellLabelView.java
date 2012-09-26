package org.trinity.render.swt.lnf.impl;

import javax.inject.Named;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.render.swt.api.SwtPaintContext;
import org.trinity.render.swt.api.Visual;
import org.trinity.shellplugin.widget.api.binding.ViewAttributeSlot;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Named("ShellLabelView")
public class ShellLabelView extends ShellWidgetViewImpl {

	private Label label;

	@Override
	protected Visual createVisual(Composite parentVisual) {
		Visual visual = super.createVisual(parentVisual);
		label = new Label(	visual,
							SWT.LEAD);
		visual.setBody(visual);

		return visual;
	}

	@ViewAttributeSlot("text")
	public void handleTextChanged(	PaintableSurfaceNode paintableSurfaceNode,
									SwtPaintContext paintContext,
									String text) {
		label.setText(text);
	}
}
