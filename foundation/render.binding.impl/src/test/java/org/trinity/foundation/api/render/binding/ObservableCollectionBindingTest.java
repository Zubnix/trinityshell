package org.trinity.foundation.api.render.binding;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.trinity.foundation.api.render.binding.view.EventSignalFilter;
import org.trinity.foundation.api.render.binding.view.delegate.ChildViewDelegate;
import org.trinity.foundation.api.render.binding.view.delegate.PropertySlotInvocatorDelegate;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.Injector;

public class ObservableCollectionBindingTest {

	@Test
	public void testBinding() throws ExecutionException, InterruptedException {
		final Model model = new Model();
		final View view = new View();

		final PropertySlotInvocatorDelegate propertySlotInvocatorDelegate = mock(PropertySlotInvocatorDelegate.class);
		final ChildViewDelegate childViewDelegate = mock(ChildViewDelegate.class);

		final ListenableFuture<CollectionElementView> viewFuture = Futures.immediateFuture(new CollectionElementView());
		when(childViewDelegate.newView(	view,
										CollectionElementView.class,
										0)).thenReturn(viewFuture);

		final Injector injector = mock(Injector.class);
		final EventSignalFilter eventSignalFilter = mock(EventSignalFilter.class);
		when(injector.getInstance(EventSignalFilter.class)).thenReturn(eventSignalFilter);

		final Binder binder = new BinderImpl(	injector,
												propertySlotInvocatorDelegate,
												childViewDelegate);
		binder.bind(MoreExecutors.sameThreadExecutor(),
					model,
					view);

		verify(	childViewDelegate,
				times(1)).newView(	view,
									CollectionElementView.class,
									0);
	}

	@Test
	public void testInsert() throws ExecutionException, InterruptedException {
		final Model model = new Model();
		final View view = new View();

		final PropertySlotInvocatorDelegate propertySlotInvocatorDelegate = mock(PropertySlotInvocatorDelegate.class);
		final ChildViewDelegate childViewDelegate = mock(ChildViewDelegate.class);
		final ListenableFuture<CollectionElementView> viewFuture = Futures.immediateFuture(new CollectionElementView());

		when(childViewDelegate.<CollectionElementView> newView(	view,
																CollectionElementView.class,
																0)).thenReturn(viewFuture);
		when(childViewDelegate.<CollectionElementView> newView(	view,
																CollectionElementView.class,
																1)).thenReturn(viewFuture);

		final Injector injector = mock(Injector.class);
		final EventSignalFilter eventSignalFilter = mock(EventSignalFilter.class);
		when(injector.getInstance(EventSignalFilter.class)).thenReturn(eventSignalFilter);

		final Binder binder = new BinderImpl(	injector,
												propertySlotInvocatorDelegate,
												childViewDelegate);
		binder.bind(MoreExecutors.sameThreadExecutor(),
					model,
					view);

		final DummySubModel childDummySubModel = new DummySubModel();

		model.getDummySubModels().add(childDummySubModel);

		verify(	childViewDelegate,
				times(1)).newView(	view,
															CollectionElementView.class,
															0);
		verify(	childViewDelegate,
				times(1)).newView(	view,
															CollectionElementView.class,
															1);

	}

	@Test
	public void testDelete() throws ExecutionException, InterruptedException {
		final Model model = new Model();
		final View view = new View();

		final PropertySlotInvocatorDelegate propertySlotInvocatorDelegate = mock(PropertySlotInvocatorDelegate.class);
		final ChildViewDelegate childViewDelegate = mock(ChildViewDelegate.class);
		final CollectionElementView collectionElementView = new CollectionElementView();

		final ListenableFuture<CollectionElementView> viewFuture = Futures.immediateFuture(collectionElementView);

		when(childViewDelegate.<CollectionElementView> newView(	view,
																CollectionElementView.class,
																0)).thenReturn(viewFuture);

		final Injector injector = mock(Injector.class);
		final EventSignalFilter eventSignalFilter = mock(EventSignalFilter.class);
		when(injector.getInstance(EventSignalFilter.class)).thenReturn(eventSignalFilter);

		final Binder binder = new BinderImpl(	injector,
												propertySlotInvocatorDelegate,
												childViewDelegate);
		binder.bind(MoreExecutors.sameThreadExecutor(),
					model,
					view);

		model.getDummySubModels().remove(0);
		verify(	childViewDelegate,
				times(1)).destroyView(	view,
										collectionElementView,
										0);
	}

	@Test
	public void testReorder() throws ExecutionException, InterruptedException {
		final Model model = new Model();
		final View view = new View();

		final PropertySlotInvocatorDelegate propertySlotInvocatorDelegate = mock(PropertySlotInvocatorDelegate.class);
		final ChildViewDelegate childViewDelegate = mock(ChildViewDelegate.class);
		final ListenableFuture<CollectionElementView> viewFuture = Futures.immediateFuture(new CollectionElementView());

		when(childViewDelegate.<CollectionElementView> newView(	view,
																CollectionElementView.class,
																0)).thenReturn(viewFuture);
		when(childViewDelegate.<CollectionElementView> newView(	view,
																CollectionElementView.class,
																1)).thenReturn(viewFuture);
		when(childViewDelegate.<CollectionElementView> newView(	view,
																CollectionElementView.class,
																2)).thenReturn(viewFuture);

		final Injector injector = mock(Injector.class);
		final EventSignalFilter eventSignalFilter = mock(EventSignalFilter.class);
		when(injector.getInstance(EventSignalFilter.class)).thenReturn(eventSignalFilter);

		final Binder binder = new BinderImpl(	injector,
												propertySlotInvocatorDelegate,
												childViewDelegate);
		binder.bind(MoreExecutors.sameThreadExecutor(),
					model,
					view);

		final DummySubModel childDummySubModel0 = new DummySubModel();
		final DummySubModel childDummySubModel1 = new DummySubModel();

		model.getDummySubModels().add(childDummySubModel0);
		model.getDummySubModels().set(	1,
										childDummySubModel1);

	}
}
