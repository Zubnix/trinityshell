package org.trinity.foundation.api.render.binding;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.trinity.foundation.api.render.binding.view.ViewElementTypes;
import org.trinity.foundation.api.render.binding.view.delegate.ChildViewDelegate;
import org.trinity.foundation.api.render.binding.view.delegate.InputListenerInstallerDelegate;
import org.trinity.foundation.api.render.binding.view.delegate.PropertySlotInvocatorDelegate;

public class PropertyBindingTest {

	@Test
	public void testBoundProperty() throws ExecutionException, NoSuchMethodException, SecurityException {
		final Model model = new Model();
		final View view = new View();

		final PropertySlotInvocatorDelegate propertySlotInvocatorDelegate = mock(PropertySlotInvocatorDelegate.class);
		final ViewElementTypes viewElementTypes = mock(ViewElementTypes.class);
		when(viewElementTypes.getViewElementTypes()).thenReturn(new Class<?>[] { Object.class });
		final InputListenerInstallerDelegate inputListenerInstallerDelegate = mock(InputListenerInstallerDelegate.class);
		final ChildViewDelegate childViewDelegate = mock(ChildViewDelegate.class);
		when(childViewDelegate.newView(	view,
										CollectionElementView.class,
										0)).thenReturn(new CollectionElementView());
		final Binder binder = new BinderImpl(	propertySlotInvocatorDelegate,
												inputListenerInstallerDelegate,
												childViewDelegate,
												viewElementTypes);
		binder.bind(model,
					view);
		binder.updateBinding(	model,
								"dummySubModel");

		// then
		// once for binding init
		verify(	propertySlotInvocatorDelegate,
				times(1)).invoke(	view.getMouseInputSubView(),
									SubView.class.getMethod("handleStringProperty",
															String.class),
									"false");
		// once for binding init, once for datacontext value update
		verify(	propertySlotInvocatorDelegate,
				times(2)).invoke(	view.getKeyInputSubView(),
									SubView.class.getMethod("handleBooleanProperty",
															boolean.class),
									false);
		// once for binding init
		verify(	propertySlotInvocatorDelegate,
				times(1)).invoke(	view,
									View.class.getMethod(	"setClassName",
															String.class),
									model.getClass().getName());
	}

	@Test
	public void testUpdateAndConvertedProperty() throws ExecutionException, NoSuchMethodException, SecurityException {
		// given
		final Model model = new Model();
		final View view = new View();

		final PropertySlotInvocatorDelegate propertySlotInvocatorDelegate = mock(PropertySlotInvocatorDelegate.class);
		final ViewElementTypes viewElementTypes = mock(ViewElementTypes.class);
		when(viewElementTypes.getViewElementTypes()).thenReturn(new Class<?>[] { Object.class });
		final InputListenerInstallerDelegate inputListenerInstallerDelegate = mock(InputListenerInstallerDelegate.class);
		final ChildViewDelegate childViewDelegate = mock(ChildViewDelegate.class);
		when(childViewDelegate.newView(	view,
										CollectionElementView.class,
										0)).thenReturn(new CollectionElementView());
		final Binder binder = new BinderImpl(	propertySlotInvocatorDelegate,
												inputListenerInstallerDelegate,
												childViewDelegate,
												viewElementTypes);

		// when
		binder.bind(model,
					view);
		binder.updateBinding(	model.getOtherSubModel().getSubSubModel(),
								"booleanProperty");
		binder.updateBinding(	model.getDummySubModel(),
								"booleanProperty");

		// then
		// once for binding init, once for property udpdate
		verify(	propertySlotInvocatorDelegate,
				times(2)).invoke(	view.getMouseInputSubView(),
									SubView.class.getMethod("handleStringProperty",
															String.class),
									"false");
		// once for binding init, once for property update
		verify(	propertySlotInvocatorDelegate,
				times(2)).invoke(	view.getKeyInputSubView(),
									SubView.class.getMethod("handleBooleanProperty",
															boolean.class),
									false);
		// once for binding init
		verify(	propertySlotInvocatorDelegate,
				times(1)).invoke(	view,
									View.class.getMethod(	"setClassName",
															String.class),
									model.getClass().getName());
	}
}
