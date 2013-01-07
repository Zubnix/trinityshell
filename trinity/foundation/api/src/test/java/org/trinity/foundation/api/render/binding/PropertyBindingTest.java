package org.trinity.foundation.api.render.binding;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.trinity.foundation.api.render.binding.view.ViewElementTypes;
import org.trinity.foundation.api.render.binding.view.delegate.PropertySlotInvocatorDelegate;

public class PropertyBindingTest {

	@Test
	public void testBoundProperty() throws ExecutionException {
		final Model model = new Model();
		final View view = new View();

		final BindingAnnotationScanner bindingAnnotationScanner = mock(BindingAnnotationScanner.class);
		final PropertySlotInvocatorDelegate propertySlotInvocatorDelegate = mock(PropertySlotInvocatorDelegate.class);
		final ViewElementTypes viewElementTypes = mock(ViewElementTypes.class);
		when(viewElementTypes.getViewElementTypes()).thenReturn(new Class<?>[] { Object.class });
		final Binder binder = new Binder(	bindingAnnotationScanner,
											propertySlotInvocatorDelegate,
											null,
											null,
											viewElementTypes);
		binder.bind(model,
					view);
		binder.updateBinding(	model,
								"dummySubModel");
	}

	@Test
	public void testConvertedProperty() throws ExecutionException {
		final Model model = new Model();
		final View view = new View();

		final BindingAnnotationScanner bindingAnnotationScanner = mock(BindingAnnotationScanner.class);
		final PropertySlotInvocatorDelegate propertySlotInvocatorDelegate = mock(PropertySlotInvocatorDelegate.class);
		final ViewElementTypes viewElementTypes = mock(ViewElementTypes.class);
		when(viewElementTypes.getViewElementTypes()).thenReturn(new Class<?>[] { Object.class });
		final Binder binder = new Binder(	bindingAnnotationScanner,
											propertySlotInvocatorDelegate,
											null,
											null,
											viewElementTypes);
		binder.bind(model,
					view);
		binder.updateBinding(	model,
								"booleanProperty");
	}
}
