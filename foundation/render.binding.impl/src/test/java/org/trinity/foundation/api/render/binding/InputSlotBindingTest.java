//package org.trinity.foundation.api.render.binding;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.concurrent.ExecutionException;
//
//import org.junit.Test;
//import org.trinity.foundation.api.render.binding.view.ViewElementTypes;
//import org.trinity.foundation.api.render.binding.view.delegate.ChildViewDelegate;
//import org.trinity.foundation.api.render.binding.view.delegate.PropertySlotInvocatorDelegate;
//
//import com.google.common.util.concurrent.ListenableFuture;
//
//public class InputSlotBindingTest {
//
//	@Test
//	public void testInputSlotBinding() throws ExecutionException, InterruptedException {
//		final Model model = new Model();
//		final View view = new View();
//
//		final PropertySlotInvocatorDelegate propertySlotInvocatorDelegate = mock(PropertySlotInvocatorDelegate.class);
//		final ViewElementTypes viewElementTypes = mock(ViewElementTypes.class);
//		when(viewElementTypes.getViewElementTypes()).thenReturn(new Class<?>[] { Object.class });
//		final EventListenerInstallerDelegate eventListenerInstallerDelegate = mock(EventListenerInstallerDelegate.class);
//		final ChildViewDelegate childViewDelegate = mock(ChildViewDelegate.class);
//		final ListenableFuture<CollectionElementView> viewFuture = mock(ListenableFuture.class);
//		when(viewFuture.get()).thenReturn(new CollectionElementView());
//		when(childViewDelegate.newView(	view,
//										CollectionElementView.class,
//										0)).thenReturn(viewFuture);
//		final Binder binder = new BinderImpl(	propertySlotInvocatorDelegate,
//				eventListenerInstallerDelegate,
//												childViewDelegate,
//												viewElementTypes);
//		binder.bind(model,
//					view);
//
//		verify(eventListenerInstallerDelegate,
//				times(1)).installViewEventListener(KeyboardInput.class,
//				view.getKeyInputSubView(),
//				model.getDummySubModel(),
//				"onKey");
//	}
//
//	@Test
//	public void testInputSlotBindingRenewal() throws ExecutionException, InterruptedException {
//		final Model model = new Model();
//		final View view = new View();
//
//		final PropertySlotInvocatorDelegate propertySlotInvocatorDelegate = mock(PropertySlotInvocatorDelegate.class);
//		final ViewElementTypes viewElementTypes = mock(ViewElementTypes.class);
//		when(viewElementTypes.getViewElementTypes()).thenReturn(new Class<?>[] { Object.class });
//		final EventListenerInstallerDelegate eventListenerInstallerDelegate = mock(EventListenerInstallerDelegate.class);
//		final ChildViewDelegate childViewDelegate = mock(ChildViewDelegate.class);
//		final ListenableFuture<CollectionElementView> viewFuture = mock(ListenableFuture.class);
//		when(viewFuture.get()).thenReturn(new CollectionElementView());
//		when(childViewDelegate.newView(	view,
//										CollectionElementView.class,
//										0)).thenReturn(viewFuture);
//		final Binder binder = new BinderImpl(	propertySlotInvocatorDelegate,
//				eventListenerInstallerDelegate,
//												childViewDelegate,
//												viewElementTypes);
//		binder.bind(model,
//					view);
//		binder.updateBinding(	model,
//								"otherSubModel");
//		verify(eventListenerInstallerDelegate,
//				times(2)).installViewEventListener(PointerInput.class,
//				view.getMouseInputSubView(),
//				model.getOtherSubModel().getSubSubModel(),
//				"onClick");
//	}
//}
