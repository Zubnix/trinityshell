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

public class ViewPropertyBindingTest {

	@Test
	public void testViewReferenceDiscovery() throws ExecutionException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		BindingDiscovery bindingDiscovery = new BindingDiscovery(null);
		Optional<Method> dummyView = bindingDiscovery.lookupViewReference(DummyPaintableSurfaceNode.class);
		DummyPaintableSurfaceNode dummyPaintableSurfaceNode = new DummyPaintableSurfaceNode();
		Object view = dummyView.get().invoke(dummyPaintableSurfaceNode);
		assertNotNull(view);
	}

	@Test
	public void testViewSlotDiscovery() throws ExecutionException {
		BindingDiscovery bindingDiscovery = new BindingDiscovery(null);
		Optional<Method> objectSlot = bindingDiscovery.lookupViewPropertySlot(	DummyView.class,
																		"object");
		assertNotNull(objectSlot.orNull());

		Optional<Method> namelessSlot = bindingDiscovery.lookupViewPropertySlot(DummyView.class,
																		"nameless");
		assertNotNull(namelessSlot.orNull());
	}

	@Test
	public void testPrimitiveBooleanViewProperty() throws Throwable {

		ViewSlotInvocationHandler viewSlotInvocationHandler = mock(ViewSlotInvocationHandler.class);

		BindingDiscovery bindingDiscovery = new BindingDiscovery(viewSlotInvocationHandler);

		ViewPropertySignalDispatcher viewPropertySignalDispatcher = new ViewPropertySignalDispatcher();
		viewPropertySignalDispatcher.setViewPropertyDiscovery(bindingDiscovery);

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
