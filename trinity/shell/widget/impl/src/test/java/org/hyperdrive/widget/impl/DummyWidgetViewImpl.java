package org.hyperdrive.widget.impl;

import org.trinity.core.geometry.api.Rectangle;
import org.trinity.foundation.display.api.ResourceHandle;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.Paintable;
import org.trinity.shell.widget.api.ViewImplementation;
import org.trinity.shell.widget.api.Widget;

@ViewImplementation(Widget.View.class)
public class DummyWidgetViewImpl implements Widget.View {

	@Override
	public PaintInstruction<ResourceHandle, ?> doCreate(Rectangle form,
			boolean visible, Paintable parentPaintable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaintInstruction<Void, ?> doDestroy() {
		// TODO Auto-generated method stub
		return null;
	}

}
