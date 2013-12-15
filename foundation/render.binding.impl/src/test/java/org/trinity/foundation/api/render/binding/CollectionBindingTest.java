package org.trinity.foundation.api.render.binding;

import ca.odell.glazedlists.DebugList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEventListener;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.delegate.SubViewModelDelegate;

import java.util.Collection;
import java.util.LinkedList;

import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CollectionBindingTest {

	public static class ChildViewModel {

	}

	@Spy
	private final EventList<Object> eventList = new DebugList<>();
	private final Object            dataModel = new Object() {
		public EventList<Object> getFooCollection() {
			return CollectionBindingTest.this.eventList;
		}
	};
	@Mock
	private Object viewModel;
	@Mock
	private Object subViewModel0;
	@Mock
	private Object subViewModel1;

	@Mock
	private ViewBinder           viewBinder;
	@Mock
	private SubViewModelDelegate subViewModelDelegate;
	@Mock
	private ViewBindingMeta      viewBindingMeta;

	private final ListeningExecutorService dataModelExecutor = MoreExecutors.sameThreadExecutor();
	@Mock
	private ObservableCollection observableCollection;

	private CollectionBinding collectionBinding;

	@Before
	public void setUp() {
		when(this.observableCollection.view()).thenReturn((Class) ChildViewModel.class);
		when(this.observableCollection.value()).thenReturn("fooCollection");

		when(this.viewBindingMeta.getViewModel()).thenReturn(this.viewModel);
		when(this.viewBindingMeta.resolveDataModelChain((LinkedList<DataModelProperty>) any())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable {
				final Object arg0 = invocation.getArguments()[0];
				final LinkedList<DataModelProperty> propertyChain = (LinkedList<DataModelProperty>) arg0;
				propertyChain.add(new ConstantDataModelProperty(CollectionBindingTest.this.dataModel));
				return TRUE;
			}
		});
		when(this.viewBindingMeta.appendDataModelPropertyChain((LinkedList<DataModelProperty>) any(),
															   (String) any())).thenReturn(TRUE);

		when(this.subViewModelDelegate.newView(any(),
											   (Class<Object>) any(),
											   anyInt())).thenReturn(Futures.immediateFuture(this.subViewModel0),
																	 Futures.immediateFuture(this.subViewModel1));

		this.collectionBinding = new CollectionBinding(this.viewBinder,
													   this.subViewModelDelegate,
													   this.viewBindingMeta,
													   this.dataModelExecutor,
													   this.observableCollection);
	}

	@Test
	public void testBind() {
		//given
		//a data model with an observable collection with elements
		//a view model with a viewable collection

		//when
		//the collection binding is bound
		final Collection<DataModelProperty> dataModelProperties = this.collectionBinding.bind();

		//then
		//the observable collection binding is mapped to the observable collection elements
		//listeners are installed to monitor changes in the collection
		assertEquals(2,
					 dataModelProperties.size());
		verify(this.eventList).addListEventListener(isA(ListEventListener.class));
	}

	@Test
	public void testElementAdd() {
		//given
		//a data model with an observable collection with elements
		//a view model with a viewable collection
		//a collection binding
		this.collectionBinding.bind();

		//when
		//an element is added to the collection
		final Object element0 = new Object();
		final Object element1 = new Object();
		this.eventList.add(element0);
		this.eventList.add(element1);

		//then
		//the viewable collection is updated
		verify(this.subViewModelDelegate).newView(this.viewModel,
												  (Class) ChildViewModel.class,
												  0);
		verify(this.subViewModelDelegate).newView(this.viewModel,
												  (Class) ChildViewModel.class,
												  1);

		verify(this.viewBinder).bind(this.dataModelExecutor,
									 element0,
									 this.subViewModel0);
		verify(this.viewBinder).bind(this.dataModelExecutor,
									 element1,
									 this.subViewModel1);
	}

	@Test
	public void testElementRemoved() {
		//given
		//a data model with an observable collection with elements
		//a view model with a viewable collection
		//a collection binding with elements
		final Object element0 = new Object();
		final Object element1 = new Object();
		this.eventList.add(element0);
		this.eventList.add(element1);

		when(this.viewBinder.unbind(this.dataModelExecutor,
									element0,
									this.subViewModel0)).thenReturn(Futures.<Void>immediateFuture(null));

		this.collectionBinding.bind();


		//when
		//an element is removed
		this.eventList.remove(element0);

		//then
		//the viewable collection is updated
		verify(this.subViewModelDelegate).destroyView(this.viewModel,
													  this.subViewModel0,
													  0);
		verify(this.viewBinder).unbind(this.dataModelExecutor,
									   element0,
									   this.subViewModel0);
	}

	@Test
	public void testElementSort() {
		//given
		//a data model with an observable collection with elements
		//a view model with a viewable collection
		//a collection binding with elements
		final Object element0 = new Object();
		final Object element1 = new Object();
		this.eventList.add(element0);
		this.eventList.add(element1);

		when(this.viewBinder.unbind(this.dataModelExecutor,
									element0,
									this.subViewModel0)).thenReturn(Futures.<Void>immediateFuture(null));
		when(this.viewBinder.unbind(this.dataModelExecutor,
									element1,
									this.subViewModel1)).thenReturn(Futures.<Void>immediateFuture(null));

		this.collectionBinding.bind();

		//when the order of elements is changed
		this.eventList.set(0, element1);
		this.eventList.set(1,element0);

		//then
		//the viewable collection is updated
		verify(this.viewBinder).unbind(this.dataModelExecutor,
									   element0,
									   this.subViewModel0);
		verify(this.viewBinder).bind(this.dataModelExecutor,
									   element1,
									   this.subViewModel0);

		verify(this.viewBinder).unbind(this.dataModelExecutor,
									   element1,
									   this.subViewModel1);
		verify(this.viewBinder).bind(this.dataModelExecutor,
									 element0,
									 this.subViewModel1);
	}
}
