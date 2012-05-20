package org.hyperdrive.widget.impl;

import org.hyperdrive.widget.api.ViewReference;
import org.hyperdrive.widget.api.Widget;
import org.hyperdrive.widget.impl.BaseWidget;

public class ChildWidget extends BaseWidget {

	public interface View extends Widget.View {

	}

	@ViewReference
	private View view;

	@Override
	public View getView() {
		return view;
	}
}
