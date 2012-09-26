package org.trinity.render.swt.api;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

public class Visual extends Composite {

	private Control body;

	public Visual(Composite parent, int style) {
		super(	parent,
				style);
	}

	public Control getBody() {
		return body;
	}

	public void setBody(Control body) {
		super.setLayout(new VisualBodyLayout(body));
	}

	@Override
	public void setLayout(Layout layout) {
		throw new UnsupportedOperationException("Changing layout manager not supported. Use this Visuals's body to make changes.");
	}
}