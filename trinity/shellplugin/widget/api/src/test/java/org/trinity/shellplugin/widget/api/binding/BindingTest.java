package org.trinity.shellplugin.widget.api.binding;

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

public class BindingTest {

	@Test
	public void testViewReferenceDiscovery() throws ExecutionException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		ViewPropertyDiscovery viewPropertyDiscovery = new ViewPropertyDiscovery(null);
		Optional<Method> dummyView = viewPropertyDiscovery.lookupViewReference(DummyPaintableSurfaceNode.class);
		DummyPaintableSurfaceNode dummyPaintableSurfaceNode = new DummyPaintableSurfaceNode();
		Object view = dummyView.get().invoke(dummyPaintableSurfaceNode);
		assertNotNull(view);
	}

	@Test
	public void testViewSlotDiscovery() throws ExecutionException {
		ViewPropertyDiscovery viewPropertyDiscovery = new ViewPropertyDiscovery(null);
		Optional<Method> objectSlot = viewPropertyDiscovery.lookupViewSlot(	DummyView.class,
																			"object");
		assertNotNull(objectSlot.orNull());

		Optional<Method> namelessSlot = viewPropertyDiscovery.lookupViewSlot(	DummyView.class,
																				"nameless");
		assertNotNull(namelessSlot.orNull());
	}

	@Test
	public void testPrimitiveBooleanViewProperty() throws Throwable {

		ViewSlotInvocationHandler viewSlotInvocationHandler = mock(ViewSlotInvocationHandler.class);

		ViewPropertyDiscovery viewPropertyDiscovery = new ViewPropertyDiscovery(viewSlotInvocationHandler);

		ViewPropertySignalDispatcher viewPropertySignalDispatcher = new ViewPropertySignalDispatcher();
		viewPropertySignalDispatcher.setViewPropertyDiscovery(viewPropertyDiscovery);

		MethodInvocation methodInvocation = mock(MethodInvocation.class);
		DummyPaintableSurfaceNode dummyPaintableSurfaceNode = new DummyPaintableSurfaceNode();
		when(methodInvocation.getThis()).thenReturn(dummyPaintableSurfaceNode);
		Method setPrimitiveBooleanMethod = DummyPaintableSurfaceNode.class.getMethod(	"setPrimitiveBoolean",
																						boolean.class);
		when(methodInvocation.getMethod()).thenReturn(setPrimitiveBooleanMethod);

		viewPropertySignalDispatcher.invoke(methodInvocation);

		Method isPrimitiveBooleanMethod = DummyPaintableSurfaceNode.class.getMethod("isPrimitiveBoolean");
		ViewProperty viewProperty = isPrimitiveBooleanMethod.getAnnotation(ViewProperty.class);
		Object view = dummyPaintableSurfaceNode.getView();
		Method viewSlot = DummyView.class.getMethod("viewSlotPrimitiveBoolean",
													boolean.class);
		boolean argument = dummyPaintableSurfaceNode.isPrimitiveBoolean();
		verify(viewSlotInvocationHandler).invokeSlot(	dummyPaintableSurfaceNode,
														viewProperty,
														view,
														viewSlot,
														argument);
	}
}
