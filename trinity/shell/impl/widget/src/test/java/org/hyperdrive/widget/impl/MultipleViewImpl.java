package org.hyperdrive.widget.impl;

import org.hyperdrive.widget.api.ViewImplementation;
import org.hyperdrive.widget.api.Widget;
import org.trinity.core.display.api.ResourceHandle;
import org.trinity.core.geometry.api.Rectangle;
import org.trinity.core.render.api.PaintInstruction;
import org.trinity.core.render.api.Paintable;

@ViewImplementation({ Widget.View.class, ChildWidget.View.class })
public class MultipleViewImpl implements Widget.View, ChildWidget.View {

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
