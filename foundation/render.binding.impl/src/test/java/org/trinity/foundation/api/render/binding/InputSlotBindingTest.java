package org.trinity.foundation.api.render.binding;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.trinity.foundation.api.render.binding.view.delegate.PropertySlotInvocationDelegate;
import org.trinity.foundation.api.render.binding.view.delegate.Signal;
import org.trinity.foundation.api.render.binding.view.EventSignalFilter;
import org.trinity.foundation.api.render.binding.view.delegate.SubViewModelDelegate;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.Injector;

public class InputSlotBindingTest {

	@Test
	public void testInputSlotBinding() throws ExecutionException, InterruptedException {
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

		final ViewBinder viewBinder = new ViewBinderImpl(	injector,
                                                 propertySlotInvocationDelegate,
                                                subViewModelDelegate);
		viewBinder.bind(MoreExecutors.sameThreadExecutor(),
					model,
					view);

		verify(	eventSignalFilter,
				times(1)).installFilter(eq(view.getKeyInputSubView()),
										any(Signal.class));
	}

	@Test
	public void testInputSlotBindingRenewal() throws ExecutionException, InterruptedException {
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

		final ViewBinder viewBinder = new ViewBinderImpl(	injector,
                                                 propertySlotInvocationDelegate,
                                                subViewModelDelegate);
		viewBinder.bind(MoreExecutors.sameThreadExecutor(),
					model,
					view);
		viewBinder.updateDataModelBinding(MoreExecutors.sameThreadExecutor(),
                                      model,
                                      "otherSubModel");

		// once for bind
		verify(	eventSignalFilter,
				times(1)).installFilter(eq(view.getKeyInputSubView()),
										any(Signal.class));
		// once for bind, once for bind update
		verify(	eventSignalFilter,
				times(2)).installFilter(eq(view.getMouseInputSubView()),
										any(Signal.class));
	}
}
