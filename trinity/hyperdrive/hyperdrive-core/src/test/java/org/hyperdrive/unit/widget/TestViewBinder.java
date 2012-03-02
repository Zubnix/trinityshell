package org.hyperdrive.unit.widget;

import java.util.concurrent.ExecutionException;

import org.hydrogen.paintinterface.Painter;
import org.hyperdrive.widget.ViewBinder;
import org.hyperdrive.widget.Widget;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class TestViewBinder {

	static {
		ViewBinder.bindView(DummyWidget.View.class, DummyWidgetView.class);
	}

	@Test
	public void testBinderPaintInstructionDelegation()
			throws InterruptedException, ExecutionException {

		final Widget widget = Mockito.mock(DummyWidget.class);
		final Painter painter = Mockito.mock(Painter.class);

		Mockito.when(widget.getPainter()).thenReturn(painter);

		final Widget.View view = ViewBinder.createView(widget,
				DummyWidget.View.class);

		Assert.assertNotNull(view);

		view.doCreate(null);

		Mockito.verify(painter, Mockito.times(1)).paint(
				DummyWidgetView.CREATE_PAINT_CALL);
	}

	@Test
	public void testViewBindingProvider() {

		final DummyWidget widget = Mockito.mock(DummyWidget.class);
		final Painter painter = Mockito.mock(Painter.class);

		Mockito.when(widget.getPainter()).thenReturn(painter);

		final Widget.View nullView = ViewBinder.createView(widget,
				Widget.View.class);
		nullView.doCreate(null);

		final Widget.View view = ViewBinder.createView(widget,
				DummyWidget.View.class);
		Assert.assertNotNull(view);

		Assert.assertTrue(view instanceof DummyWidget.View);
	}

	@Test
	public void testWidgetViewLookup() {
		final DummyWidget widget = new DummyWidget();

		Assert.assertNotNull(widget.getView());

		Assert.assertTrue(widget.getView() instanceof DummyWidget.View);
	}
}
