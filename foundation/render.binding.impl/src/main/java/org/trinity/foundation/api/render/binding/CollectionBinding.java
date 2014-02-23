package org.trinity.foundation.api.render.binding;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import com.google.common.base.Optional;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.delegate.SubViewModelDelegate;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

public class CollectionBinding implements ViewBinding {

	private static final Logger LOG = LoggerFactory.getLogger(CollectionBinding.class);

	//Table<DataModel,Index,ChildViewModel>
	private final Table<Object, Integer, Object> dataModelElementToViewModelElement = HashBasedTable.create();


	private final ViewBindingMeta          viewBindingMeta;
	private final ViewBinder               viewBinder;
	private final SubViewModelDelegate     subViewModelDelegate;
	private final ListeningExecutorService dataModelExecutor;
	private final ObservableCollection     observableCollection;

	@Inject
	CollectionBinding(final ViewBinder viewBinder,
					  final SubViewModelDelegate subViewModelDelegate,
					  //TODO autofactory
					  final ViewBindingMeta viewBindingMeta,
					  //TODO autofactory
					  final ListeningExecutorService dataModelExecutor,
					  //TODO autofactory
					  final ObservableCollection observableCollection) {
		this.viewBindingMeta = viewBindingMeta;
		this.viewBinder = viewBinder;
		this.subViewModelDelegate = subViewModelDelegate;
		this.dataModelExecutor = dataModelExecutor;
		this.observableCollection = observableCollection;
	}

	@Override
	public ViewBindingMeta getViewBindingMeta() {
		return this.viewBindingMeta;
	}

	@Override
	public Collection<DataModelProperty> bind() {

		final LinkedList<DataModelProperty> properties = new LinkedList<>();
		final boolean commonDataModelContextResolved = this.viewBindingMeta.resolveDataModelChain(properties);

		if(!commonDataModelContextResolved) {
			return properties;
		}

		final String collectionDataModelContext = this.observableCollection.dataModelContext();
		final boolean dataModelContextResolved = this.viewBindingMeta.appendDataModelPropertyChain(properties,
																								   collectionDataModelContext);

		if(!dataModelContextResolved) {
			return properties;
		}

		final String collectionPropertyName = this.observableCollection.value();
		final Optional<Object> lastPropertyValue = properties.getLast().getPropertyValue();

		if(!lastPropertyValue.isPresent()) {
			return properties;
		}

		final DataModelProperty collectionProperty = new RelativeDataModelProperty( lastPropertyValue.get(),
																			        collectionPropertyName);
		properties.add(collectionProperty);
		final Optional<Object> collectionPropertyValue = collectionProperty.getPropertyValue();

		if(!collectionPropertyValue.isPresent()) {
			return properties;
		}

		final Object collection = collectionPropertyValue.get();
		bindObservableCollection(collection);

		return properties;
	}

	@Override
	public void unbind() {

	}

	private void bindObservableCollection(final Object collection) {

		final Object viewModel = this.viewBindingMeta.getViewModel();

		checkArgument(collection instanceof EventList,
					  format("Observable collection must be bound to a property of type %s @ viewModel: %s, observable collection: %s",
							 EventList.class.getName(),
							 viewModel,
							 this.observableCollection));

		final EventList<?> collectionModel = (EventList<?>) collection;

		//listen for collection changes
		registerEventListener(collectionModel);

		//for each element, construct a new child view & bind it to the element
		for(int i = 0; i < collectionModel.size(); i++) {
			final Object childDataModel = collectionModel.get(i);
			createCollectionElementView(childDataModel,
										i);
		}
	}

	private void registerEventListener(final EventList<?> collectionModel) {
		collectionModel.addListEventListener(new ListEventListener<Object>() {
			// We use a shadow list because glazedlists does not
			// give us the deleted object...
			private final List<Object> shadowChildDataModelList = new ArrayList<>(collectionModel);

			@Override
			public void listChanged(final ListEvent<Object> listChanges) {
				handleListChanged(this.shadowChildDataModelList,
								  listChanges);
			}
		});
	}

	protected void handleListChanged(final List<Object> shadowChildDataModelList,
									 final ListEvent<Object> listChanges) {
		while(listChanges.next()) {
			final int sourceIndex = listChanges.getIndex();
			final int changeType = listChanges.getType();
			final List<Object> changeList = listChanges.getSourceList();

			switch(changeType) {
				case ListEvent.DELETE: {
					handleListEventDelete(sourceIndex,
										  shadowChildDataModelList);
					break;
				}
				case ListEvent.INSERT: {
					handleListEventInsert(changeList,
										  sourceIndex,
										  shadowChildDataModelList);
					break;
				}
				case ListEvent.UPDATE: {
					handleListEventUpdate(shadowChildDataModelList,
										  sourceIndex,
										  changeList,
										  listChanges);
					break;
				}
			}
		}
	}

	private void handleListEventDelete(final int sourceIndex,
									   final List<Object> shadowChildDataModelList) {
		final Object removedChildDataModel = shadowChildDataModelList.remove(sourceIndex);
		checkNotNull(removedChildDataModel);

		final Object removedChildViewModel = this.dataModelElementToViewModelElement.remove(removedChildDataModel,
																							sourceIndex);

		this.viewBinder.unbind(
																		   removedChildDataModel,
																		   removedChildViewModel);

							CollectionBinding.this.subViewModelDelegate.destroyView(CollectionBinding.this.getViewBindingMeta().getViewModel(),
																					removedChildViewModel,
																					sourceIndex);

	}

	private void handleListEventInsert(final List<Object> changeList,
									   final int sourceIndex,
									   final List<Object> shadowChildDataModelList) {
		final Object childDataModel = changeList.get(sourceIndex);
		checkNotNull(childDataModel);

		shadowChildDataModelList.add(sourceIndex,
									 childDataModel);

		createCollectionElementView(childDataModel,
									sourceIndex);
	}

	private void createCollectionElementView(final Object elementDataModel,
											 final int elementIndex) {
		final  Object childViewModel = this.subViewModelDelegate.newView(this.getViewBindingMeta().getViewModel(),
																					  this.observableCollection.view(),
																					  elementIndex);

							CollectionBinding.this.viewBinder.bind(
																   elementDataModel,
																   childViewModel);
	}

	private void handleListEventUpdate(final List<Object> shadowChildDataModelList,
									   final int sourceIndex,
									   final List<Object> changeList,
									   final ListEvent<Object> listChanges) {
		if(listChanges.isReordering()) {
			final int[] reorderings = listChanges.getReorderMap();
			for(int i = 0; i < reorderings.length; i++) {
				final int newPosition = reorderings[i];
				final Object childDataModel = changeList.get(sourceIndex);

				shadowChildDataModelList.clear();
				shadowChildDataModelList.add(newPosition,
											 childDataModel);

				final Object changedChildViewModel = this.dataModelElementToViewModelElement.get(childDataModel,
																								 sourceIndex);

				this.subViewModelDelegate.updateChildViewPosition(this.getViewBindingMeta().getViewModel(),
																  changedChildViewModel,
																  i,
																  newPosition);
			}
		}
		else {

			final Object newChildDataModel = changeList.get(sourceIndex);
			final Object oldChildDataModel = shadowChildDataModelList.set(sourceIndex,
																		  newChildDataModel);
			checkNotNull(oldChildDataModel);
			checkNotNull(newChildDataModel);

			final Object changedChildViewModel = this.dataModelElementToViewModelElement.remove(oldChildDataModel,
																								sourceIndex);
			this.dataModelElementToViewModelElement.put(newChildDataModel,
														sourceIndex,
														changedChildViewModel);

			this.viewBinder.unbind(
								   oldChildDataModel,
								   changedChildViewModel);
			this.viewBinder.bind(
								 newChildDataModel,
								 changedChildViewModel);
		}
	}
}