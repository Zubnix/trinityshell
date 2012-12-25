package org.trinity.shellplugin.widget.api.binding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;

import com.google.common.base.Optional;

public class ViewPropertyBindingTest {

	@Test
	public void testViewReferenceDiscovery() throws ExecutionException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		final BindingDiscovery bindingDiscovery = new BindingDiscovery(null);
		final Optional<Method> dummyView = bindingDiscovery.lookupViewReference(DummyShellWidget.class);
		final DummyShellWidget mockedDummyShellWidget = mock(DummyShellWidget.class);
		final DummyView view = new DummyView();
		when(mockedDummyShellWidget.getView()).thenReturn(view);
		final DummyShellWidget dummyShellWidget = new DummyShellWidget(mockedDummyShellWidget);
		final Object discoveredView = dummyView.get().invoke(dummyShellWidget);
		assertEquals(	view,
						discoveredView);
	}

	@Test
	public void testViewSlotDiscovery() throws ExecutionException {
		final BindingDiscovery bindingDiscovery = new BindingDiscovery(null);
		final Optional<Method> objectSlot = bindingDiscovery.lookupViewPropertySlot(DummyView.class,
																					"object");
		assertNotNull(objectSlot.orNull());

		final Optional<Method> namelessSlot = bindingDiscovery.lookupViewPropertySlot(	DummyView.class,
																						"nameless");
		assertNotNull(namelessSlot.orNull());
	}

	@Test
	public void testPrimitiveBooleanViewProperty() throws Throwable {

		final ViewSlotInvocationHandler viewSlotInvocationHandler = mock(ViewSlotInvocationHandler.class);

		final BindingDiscovery bindingDiscovery = new BindingDiscovery(viewSlotInvocationHandler);

		final ViewPropertySignalDispatcher viewPropertySignalDispatcher = new ViewPropertySignalDispatcher();
		viewPropertySignalDispatcher.setViewPropertyDiscovery(bindingDiscovery);

		final MethodInvocation methodInvocation = mock(MethodInvocation.class);
		final DummyShellWidget mockedDummyShellWidget = mock(DummyShellWidget.class);
		final DummyView view = new DummyView();
		when(mockedDummyShellWidget.getView()).thenReturn(view);
		final DummyShellWidget dummyShellWidget = new DummyShellWidget(mockedDummyShellWidget);
		when(methodInvocation.getThis()).thenReturn(dummyShellWidget);
		final Method setPrimitiveBooleanMethod = DummyShellWidget.class.getMethod(	"setPrimitiveBoolean",
																					boolean.class);
		when(methodInvocation.getMethod()).thenReturn(setPrimitiveBooleanMethod);

		viewPropertySignalDispatcher.invoke(methodInvocation);

		final Method isPrimitiveBooleanMethod = DummyShellWidget.class.getMethod("isPrimitiveBoolean");
		final ViewProperty viewProperty = isPrimitiveBooleanMethod.getAnnotation(ViewProperty.class);
		final Method viewSlot = DummyView.class.getMethod(	"viewSlotPrimitiveBoolean",
															boolean.class);
		final boolean argument = dummyShellWidget.isPrimitiveBoolean();
		verify(viewSlotInvocationHandler).invokeSlot(	dummyShellWidget,
														viewProperty,
														view,
														viewSlot,
														argument);
	}
}
