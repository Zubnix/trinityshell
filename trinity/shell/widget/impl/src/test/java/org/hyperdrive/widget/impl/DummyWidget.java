package org.hyperdrive.widget.impl;

import org.trinity.shell.widget.api.ViewReference;
import org.trinity.shell.widget.impl.WidgetImpl;

public class DummyWidget extends WidgetImpl {

	@ViewReference
	private View view;

	@Override
	public View getView() {
		return view;
	}

}
