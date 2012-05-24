package org.hyperdrive.widget.impl;

import org.junit.Assert;
import org.junit.Test;
import org.trinity.shell.widget.api.ViewBinder;
import org.trinity.shell.widget.api.Widget;

public class ViewBinderTest {

	@Test
	public void singleViewImplementationTest() {
		ViewBinder.get().bind(DummyWidgetViewImpl.class);

		Widget widget = new DummyWidget();

		Widget.View view = widget.getView();

		Assert.assertTrue(view instanceof DummyWidgetViewImpl);
	}

	@Test
	public void childWidgetTest() {
		ViewBinder.get().bind(DummyChildWidgetViewImpl.class);

		ChildWidget childWidget = new ChildWidget();

		Widget.View view = childWidget.getView();

		Assert.assertTrue(view instanceof DummyChildWidgetViewImpl);
	}

	@Test
	public void multipleViewImplementationsTest() {
		ViewBinder.get().bind(MultipleViewImpl.class);

		ChildWidget childWidget = new ChildWidget();

		Widget.View childview = childWidget.getView();

		Assert.assertTrue(childview instanceof MultipleViewImpl);

		Widget widget = new DummyWidget();

		Widget.View view = widget.getView();

		Assert.assertTrue(view instanceof MultipleViewImpl);

		Assert.assertFalse(view instanceof DummyWidgetViewImpl);
	}
}
