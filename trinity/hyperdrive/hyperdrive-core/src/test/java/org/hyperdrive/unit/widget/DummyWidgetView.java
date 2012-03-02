package org.hyperdrive.unit.widget;

import org.hydrogen.displayinterface.ResourceHandle;
import org.hydrogen.paintinterface.PaintCall;
import org.hydrogen.paintinterface.PaintContext;
import org.hydrogen.paintinterface.Paintable;
import org.hyperdrive.widget.PaintInstruction;

public class DummyWidgetView implements DummyWidget.View {

	public static final ResourceHandle RETURNED_RESOURCE_HANDLE = new ResourceHandle() {
	};

	public static final PaintCall<ResourceHandle, Object> CREATE_PAINT_CALL = new PaintCall<ResourceHandle, Object>() {
		@Override
		public ResourceHandle call(final PaintContext<Object> paintContext) {
			return DummyWidgetView.RETURNED_RESOURCE_HANDLE;
		};
	};

	public static final PaintInstruction<ResourceHandle> CREATE_INSTRUCTION = new PaintInstruction<ResourceHandle>(
			DummyWidgetView.CREATE_PAINT_CALL);

	@Override
	public PaintInstruction<ResourceHandle> doCreate(
			final Paintable parentPaintable, final Object... args) {
		return DummyWidgetView.CREATE_INSTRUCTION;
	}

}
