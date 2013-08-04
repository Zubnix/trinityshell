package org.trinity.foundation.api.render.binding;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.trinity.foundation.api.render.binding.view.EventSignalFilter;
import org.trinity.foundation.api.render.binding.view.ViewElementTypes;
import org.trinity.foundation.api.render.binding.view.delegate.ChildViewDelegate;
import org.trinity.foundation.api.render.binding.view.delegate.PropertySlotInvocatorDelegate;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.Injector;

public class ObservableCollectionBindingTest {

	@Test
	public void testBinding() throws ExecutionException, InterruptedException {
		final Model model = new Model();
		final View view = new View();

		final PropertySlotInvocatorDelegate propertySlotInvocatorDelegate = mock(PropertySlotInvocatorDelegate.class);
		final ViewElementTypes viewElementTypes = mock(ViewElementTypes.class);
		when(viewElementTypes.getViewElementTypes()).thenReturn(new Class<?>[] { Object.class });
		final ChildViewDelegate childViewDelegate = mock(ChildViewDelegate.class);
		final ListenableFuture<CollectionElementView> viewFuture = mock(ListenableFuture.class);
		when(viewFuture.get()).thenReturn(new CollectionElementView());
		when(childViewDelegate.newView(	view,
										CollectionElementView.class,
										0)).thenReturn(viewFuture);

		Injector injector = mock(Injector.class);
		EventSignalFilter eventSignalFilter = mock(EventSignalFilter.class);
		when(injector.getInstance(EventSignalFilter.class)).thenReturn(eventSignalFilter);

		final Binder binder = new BinderImpl(	injector,
												propertySlotInvocatorDelegate,
												childViewDelegate,
												viewElementTypes);
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
		final ViewElementTypes viewElementTypes = mock(ViewElementTypes.class);
		when(viewElementTypes.getViewElementTypes()).thenReturn(new Class<?>[] { Object.class });
		final ChildViewDelegate childViewDelegate = mock(ChildViewDelegate.class);
		final ListenableFuture<CollectionElementView> viewFuture = mock(ListenableFuture.class);
		when(viewFuture.get()).thenReturn(new CollectionElementView());
		when(childViewDelegate.<CollectionElementView> newView(	view,
																CollectionElementView.class,
																0)).thenReturn(viewFuture);
		when(childViewDelegate.<CollectionElementView> newView(	view,
																CollectionElementView.class,
																1)).thenReturn(viewFuture);

		Injector injector = mock(Injector.class);
		EventSignalFilter eventSignalFilter = mock(EventSignalFilter.class);
		when(injector.getInstance(EventSignalFilter.class)).thenReturn(eventSignalFilter);

		final Binder binder = new BinderImpl(	injector,
												propertySlotInvocatorDelegate,
												childViewDelegate,
												viewElementTypes);
		binder.bind(MoreExecutors.sameThreadExecutor(),
					model,
					view);

		final DummySubModel childDummySubModel = new DummySubModel();

		model.getDummySubModels().add(childDummySubModel);

		verify(	childViewDelegate,
				times(1)).<CollectionElementView> newView(	view,
															CollectionElementView.class,
															0);
		verify(	childViewDelegate,
				times(1)).<CollectionElementView> newView(	view,
															CollectionElementView.class,
															1);

	}

	@Test
	public void testDelete() throws ExecutionException, InterruptedException {
		final Model model = new Model();
		final View view = new View();

		final PropertySlotInvocatorDelegate propertySlotInvocatorDelegate = mock(PropertySlotInvocatorDelegate.class);
		final ViewElementTypes viewElementTypes = mock(ViewElementTypes.class);
		when(viewElementTypes.getViewElementTypes()).thenReturn(new Class<?>[] { Object.class });
		final ChildViewDelegate childViewDelegate = mock(ChildViewDelegate.class);
		final ListenableFuture<CollectionElementView> viewFuture = mock(ListenableFuture.class);
		when(viewFuture.get()).thenReturn(new CollectionElementView());
		when(viewFuture.get()).thenReturn(new CollectionElementView());
		final CollectionElementView collectionElementView = new CollectionElementView();
		when(childViewDelegate.<CollectionElementView> newView(	view,
																CollectionElementView.class,
																0)).thenReturn(viewFuture);

		Injector injector = mock(Injector.class);
		EventSignalFilter eventSignalFilter = mock(EventSignalFilter.class);
		when(injector.getInstance(EventSignalFilter.class)).thenReturn(eventSignalFilter);

		final Binder binder = new BinderImpl(	injector,
												propertySlotInvocatorDelegate,
												childViewDelegate,
												viewElementTypes);
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
		final ViewElementTypes viewElementTypes = mock(ViewElementTypes.class);
		when(viewElementTypes.getViewElementTypes()).thenReturn(new Class<?>[] { Object.class });
		final ChildViewDelegate childViewDelegate = mock(ChildViewDelegate.class);
		final ListenableFuture<CollectionElementView> viewFuture = mock(ListenableFuture.class);
		when(viewFuture.get()).thenReturn(new CollectionElementView());
		when(childViewDelegate.<CollectionElementView> newView(	view,
																CollectionElementView.class,
																0)).thenReturn(viewFuture);
		when(childViewDelegate.<CollectionElementView> newView(	view,
																CollectionElementView.class,
																1)).thenReturn(viewFuture);
		when(childViewDelegate.<CollectionElementView> newView(	view,
																CollectionElementView.class,
																2)).thenReturn(viewFuture);

		Injector injector = mock(Injector.class);
		EventSignalFilter eventSignalFilter = mock(EventSignalFilter.class);
		when(injector.getInstance(EventSignalFilter.class)).thenReturn(eventSignalFilter);

		final Binder binder = new BinderImpl(	injector,
												propertySlotInvocatorDelegate,
												childViewDelegate,
												viewElementTypes);
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
