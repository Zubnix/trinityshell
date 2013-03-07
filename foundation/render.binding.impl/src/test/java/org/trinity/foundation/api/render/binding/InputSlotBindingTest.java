package org.trinity.foundation.api.render.binding;

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.foundation.api.display.input.PointerInput;
import org.trinity.foundation.api.render.binding.view.ViewElementTypes;
import org.trinity.foundation.api.render.binding.view.delegate.ChildViewDelegate;
import org.trinity.foundation.api.render.binding.view.delegate.InputListenerInstallerDelegate;
import org.trinity.foundation.api.render.binding.view.delegate.PropertySlotInvocatorDelegate;

import com.google.common.util.concurrent.ListenableFuture;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InputSlotBindingTest {

	@Test
	public void testInputSlotBinding() throws ExecutionException, InterruptedException {
		final Model model = new Model();
		final View view = new View();

		final PropertySlotInvocatorDelegate propertySlotInvocatorDelegate = mock(PropertySlotInvocatorDelegate.class);
		final ViewElementTypes viewElementTypes = mock(ViewElementTypes.class);
		when(viewElementTypes.getViewElementTypes()).thenReturn(new Class<?>[] { Object.class });
		final InputListenerInstallerDelegate inputListenerInstallerDelegate = mock(InputListenerInstallerDelegate.class);
		final ChildViewDelegate childViewDelegate = mock(ChildViewDelegate.class);
		final ListenableFuture<CollectionElementView> listenableFuture = mock(ListenableFuture.class);
		when(listenableFuture.get()).thenReturn(new CollectionElementView());
		when(childViewDelegate.newView(	view,
										CollectionElementView.class,
										0)).thenReturn(listenableFuture);
		final Binder binder = new BinderImpl(	propertySlotInvocatorDelegate,
												inputListenerInstallerDelegate,
												childViewDelegate,
												viewElementTypes);
		binder.bind(model,
					view);

		verify(	inputListenerInstallerDelegate,
				times(1)).installViewInputListener(	KeyboardInput.class,
													view.getKeyInputSubView(),
													model.getDummySubModel(),
													"onKey");
	}

	@Test
	public void testInputSlotBindingRenewal() throws ExecutionException, InterruptedException {
		final Model model = new Model();
		final View view = new View();

		final PropertySlotInvocatorDelegate propertySlotInvocatorDelegate = mock(PropertySlotInvocatorDelegate.class);
		final ViewElementTypes viewElementTypes = mock(ViewElementTypes.class);
		when(viewElementTypes.getViewElementTypes()).thenReturn(new Class<?>[] { Object.class });
		final InputListenerInstallerDelegate inputListenerInstallerDelegate = mock(InputListenerInstallerDelegate.class);
		final ChildViewDelegate childViewDelegate = mock(ChildViewDelegate.class);
		final ListenableFuture<CollectionElementView> listenableFuture = mock(ListenableFuture.class);
		when(listenableFuture.get()).thenReturn(new CollectionElementView());
		when(childViewDelegate.newView(	view,
										CollectionElementView.class,
										0)).thenReturn(listenableFuture);
		final Binder binder = new BinderImpl(	propertySlotInvocatorDelegate,
												inputListenerInstallerDelegate,
												childViewDelegate,
												viewElementTypes);
		binder.bind(model,
					view);
		binder.updateBinding(	model,
								"otherSubModel");
		verify(	inputListenerInstallerDelegate,
				times(2)).installViewInputListener(	PointerInput.class,
													view.getMouseInputSubView(),
													model.getOtherSubModel().getSubSubModel(),
													"onClick");
	}
}
