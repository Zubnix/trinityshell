//package org.trinity.foundation.api.render.binding;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import java.util.concurrent.ExecutionException;
//
//import org.junit.Test;
//import org.mockito.Mockito;
//import org.trinity.foundation.api.render.binding.view.ViewElementTypes;
//import org.trinity.foundation.api.render.binding.view.delegate.ChildViewDelegate;
//import org.trinity.foundation.api.render.binding.view.delegate.PropertySlotInvocatorDelegate;
//
//import com.google.common.util.concurrent.ListenableFuture;
//
//public class DataContextTest {
//
//	@Test
//	public void testDataContextNestedValueUpdate() throws ExecutionException, NoSuchMethodException, SecurityException,
//			InterruptedException {
//		final Model model = new Model();
//		final View view = new View();
//
//		final PropertySlotInvocatorDelegate propertySlotInvocatorDelegate = Mockito
//				.mock(PropertySlotInvocatorDelegate.class);
//		final ViewElementTypes viewElementTypes = Mockito.mock(ViewElementTypes.class);
//		Mockito.when(viewElementTypes.getViewElementTypes()).thenReturn(new Class<?>[] { Object.class });
//		final EventListenerInstallerDelegate eventListenerInstallerDelegate = Mockito
//				.mock(EventListenerInstallerDelegate.class);
//		final ChildViewDelegate childViewDelegate = mock(ChildViewDelegate.class);
//		final ListenableFuture<CollectionElementView> viewFuture = mock(ListenableFuture.class);
//		when(viewFuture.get()).thenReturn(new CollectionElementView());
//
//		Mockito.when(childViewDelegate.newView(	view,
//												CollectionElementView.class,
//												0)).thenReturn(viewFuture);
//		final Binder binder = new BinderImpl(	propertySlotInvocatorDelegate,
//				eventListenerInstallerDelegate,
//												childViewDelegate,
//												viewElementTypes);
//		binder.bind(model,
//					view);
//		binder.updateBinding(	model,
//								"otherSubModel");
//
//		Mockito.verify(	propertySlotInvocatorDelegate,
//						Mockito.times(2)).invoke(	view.getMouseInputSubView(),
//													SubView.class.getMethod("handleStringProperty",
//																			String.class),
//													"false");
//		Mockito.verify(eventListenerInstallerDelegate,
//						Mockito.times(2)).installViewEventListener(PointerInput.class,
//				view.getMouseInputSubView(),
//				model.getOtherSubModel().getSubSubModel(),
//				"onClick");
//	}
//}
