package org.hyperdrive.widget.impl;

import org.trinity.shell.widget.api.ViewReference;
import org.trinity.shell.widget.api.Widget;
import org.trinity.shell.widget.impl.WidgetImpl;

public class ChildWidget extends WidgetImpl {

	public interface View extends Widget.View {

	}

	@ViewReference
	private View view;

	@Override
	public View getView() {
		return view;
	}
}
