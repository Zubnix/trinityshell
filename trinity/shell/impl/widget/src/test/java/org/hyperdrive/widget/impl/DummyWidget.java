package org.hyperdrive.widget.impl;

import org.hyperdrive.widget.api.ViewReference;
import org.hyperdrive.widget.impl.BaseWidget;

public class DummyWidget extends BaseWidget {

	@ViewReference
	private View view;

	@Override
	public View getView() {
		return view;
	}

}
