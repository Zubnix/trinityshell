package org.trinity.shellplugin.widget.api.binding;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class ViewAttributeSignalDispatcherTest {

	@Test
	public void test() throws Throwable {

		final ViewSlotInvocationHandler viewSlotInvocationHandler = mock(ViewSlotInvocationHandler.class);
		final ViewAttributeSignalDispatcher viewAttributeSignalDispatcher = new ViewAttributeSignalDispatcher();
		final Field viewSlotInvocationField = ViewAttributeSignalDispatcher.class
				.getDeclaredField("viewSlotInvocationHandler");
		viewSlotInvocationField.setAccessible(true);
		viewSlotInvocationField.set(viewAttributeSignalDispatcher,
									viewSlotInvocationHandler);
		viewSlotInvocationField.setAccessible(false);

		final DummyView view = new DummyView();
		final DummyPaintableSurfaceNode dummyPaintableSurfaceNode = new DummyPaintableSurfaceNode(view);

		final MethodInvocation invocation = mock(MethodInvocation.class);
		when(invocation.getThis()).thenReturn(dummyPaintableSurfaceNode);
		final Method method = DummyPaintableSurfaceNode.class.getMethod("setBothAttributes");
		when(invocation.getStaticPart()).thenReturn(method);
		when(invocation.getMethod()).thenReturn(method);

		dummyPaintableSurfaceNode.setBothAttributes();
		viewAttributeSignalDispatcher.invoke(invocation);

		final ArgumentCaptor<ViewAttribute> viewAttributeCaptor = ArgumentCaptor.forClass(ViewAttribute.class);
		final ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);

		final Method viewSlot = DummyView.class.getMethod("dummyViewSlot");
		verify(	viewSlotInvocationHandler,
				times(2)).invokeSlot(	eq(dummyPaintableSurfaceNode),
										viewAttributeCaptor.capture(),
										eq(view),
										eq(viewSlot),
										argumentCaptor.capture());

		final List<Object> args = argumentCaptor.getAllValues();
		Assert.assertEquals("val1",
							args.get(0));
		Assert.assertEquals("val2",
							args.get(1));
	}
}
