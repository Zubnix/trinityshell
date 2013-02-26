package org.trinity.foundation.api.render.binding;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.trinity.foundation.api.display.input.PointerInput;
import org.trinity.foundation.api.render.binding.view.ViewElementTypes;
import org.trinity.foundation.api.render.binding.view.delegate.ChildViewDelegate;
import org.trinity.foundation.api.render.binding.view.delegate.InputListenerInstallerDelegate;
import org.trinity.foundation.api.render.binding.view.delegate.PropertySlotInvocatorDelegate;

public class DataContextTest {

	@Test
	public void testDataContextNestedValueUpdate() throws ExecutionException, NoSuchMethodException, SecurityException {
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
								"otherSubModel");

		verify(	propertySlotInvocatorDelegate,
				times(2)).invoke(	view.getMouseInputSubView(),
									SubView.class.getMethod("handleStringProperty",
															String.class),
									"false");
		verify(	inputListenerInstallerDelegate,
				times(2)).installInputListener(	PointerInput.class,
												view.getMouseInputSubView(),
												model.getOtherSubModel().getSubSubModel(),
												"onClick");
	}
}
