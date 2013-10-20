package org.trinity.foundation.api.render.binding;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.trinity.foundation.api.render.binding.view.EventSignalFilter;
import org.trinity.foundation.api.render.binding.view.delegate.PropertySlotInvocationDelegate;
import org.trinity.foundation.api.render.binding.view.delegate.SubViewModelDelegate;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.Injector;

public class PropertyBindingTest {

	@Test
	public void testBoundProperty() throws ExecutionException, NoSuchMethodException, SecurityException,
			InterruptedException {
		final Model model = new Model();
		final View view = new View();

		final PropertySlotInvocationDelegate propertySlotInvocationDelegate = mock(PropertySlotInvocationDelegate.class);
		final SubViewModelDelegate subViewModelDelegate = mock(SubViewModelDelegate.class);
		final ListenableFuture<CollectionElementView> viewFuture = mock(ListenableFuture.class);
		when(viewFuture.get()).thenReturn(new CollectionElementView());
		when(subViewModelDelegate.newView(	view,
										CollectionElementView.class,
										0)).thenReturn(viewFuture);
		final Injector injector = mock(Injector.class);
		final EventSignalFilter eventSignalFilter = mock(EventSignalFilter.class);
		when(injector.getInstance(EventSignalFilter.class)).thenReturn(eventSignalFilter);

		final Binder binder = new BinderImpl(	injector,
                                                 propertySlotInvocationDelegate,
                                                subViewModelDelegate);
		binder.bind(MoreExecutors.sameThreadExecutor(),
					model,
					view);
		binder.updateDataModelBinding(MoreExecutors.sameThreadExecutor(),
                                      model,
                                      "dummySubModel");

		// then
		// once for binding init
		verify(propertySlotInvocationDelegate,
				times(1)).invoke(	view.getMouseInputSubView(),
									SubView.class.getMethod("handleStringProperty",
															String.class),
									"false");
		// once for binding init, once for datacontext value update
		verify(propertySlotInvocationDelegate,
				times(2)).invoke(	view.getKeyInputSubView(),
									SubView.class.getMethod("handleBooleanProperty",
															boolean.class),
									false);
		// once for binding init
		verify(propertySlotInvocationDelegate,
				times(1)).invoke(	view,
									View.class.getMethod(	"setClassName",
															String.class),
									model.getClass().getName());
	}

	@Test
	public void testUpdateAndConvertedProperty() throws ExecutionException, NoSuchMethodException, SecurityException,
			InterruptedException {
		// given
		final Model model = new Model();
		final View view = new View();

		final PropertySlotInvocationDelegate propertySlotInvocationDelegate = mock(PropertySlotInvocationDelegate.class);
		final SubViewModelDelegate subViewModelDelegate = mock(SubViewModelDelegate.class);
		final ListenableFuture<CollectionElementView> viewFuture = mock(ListenableFuture.class);
		when(viewFuture.get()).thenReturn(new CollectionElementView());
		when(subViewModelDelegate.newView(	view,
										CollectionElementView.class,
										0)).thenReturn(viewFuture);
		final Injector injector = mock(Injector.class);
		final EventSignalFilter eventSignalFilter = mock(EventSignalFilter.class);
		when(injector.getInstance(EventSignalFilter.class)).thenReturn(eventSignalFilter);

		final Binder binder = new BinderImpl(	injector,
                                                 propertySlotInvocationDelegate,
                                                subViewModelDelegate);

		// when
		binder.bind(MoreExecutors.sameThreadExecutor(),
					model,
					view);
		binder.updateDataModelBinding(MoreExecutors.sameThreadExecutor(),
                                      model.getOtherSubModel().getSubSubModel(),
                                      "booleanProperty");
		binder.updateDataModelBinding(MoreExecutors.sameThreadExecutor(),
                                      model.getDummySubModel(),
                                      "booleanProperty");

		// then
		// once for binding init, once for property udpdate
		verify(propertySlotInvocationDelegate,
				times(2)).invoke(	view.getMouseInputSubView(),
									SubView.class.getMethod("handleStringProperty",
															String.class),
									"false");
		// once for binding init, once for property update
		verify(propertySlotInvocationDelegate,
				times(2)).invoke(	view.getKeyInputSubView(),
									SubView.class.getMethod("handleBooleanProperty",
															boolean.class),
									false);
		// once for binding init
		verify(propertySlotInvocationDelegate,
				times(1)).invoke(	view,
									View.class.getMethod(	"setClassName",
															String.class),
									model.getClass().getName());
	}
}
