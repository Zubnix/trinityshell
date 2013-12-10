package org.trinity.foundation.api.render.binding;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import com.google.common.base.Optional;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.assistedinject.Assisted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.delegate.SubViewModelDelegate;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.util.concurrent.Futures.addCallback;
import static java.lang.String.format;

public class CollectionBinding implements ViewBinding {

	private static final Logger LOG = LoggerFactory.getLogger(CollectionBinding.class);

	//BiMap<DataModel,ChildViewModel>
	private final BiMap<Object, Object> dataModelElementToViewModelElement = HashBiMap.create();

	private final ViewBindingMeta          viewBindingMeta;
	private final ViewBinder               viewBinder;
	private final SubViewModelDelegate     subViewModelDelegate;
	private final ListeningExecutorService dataModelExecutor;
	private final ObservableCollection     observableCollection;

	@Inject
	CollectionBinding(
					  final ViewBinder viewBinder,
					  final SubViewModelDelegate subViewModelDelegate,
                      @Assisted final ViewBindingMeta viewBindingMeta,
					  @Assisted final ListeningExecutorService dataModelExecutor,
					  @Assisted final ObservableCollection observableCollection) {
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

		//for each element, create a new child view & bind it to the element
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

		final Object removedChildViewModel = this.dataModelElementToViewModelElement.remove(removedChildDataModel);

		final ListenableFuture<Void> leftOverViewModelsListenableFuture = this.viewBinder.unbind(this.dataModelExecutor,
																								 removedChildDataModel,
																								 removedChildViewModel);
		addCallback(leftOverViewModelsListenableFuture,
					new FutureCallback<Void>() {
						@Override
						public void onSuccess(@Nullable final Void result) {
							CollectionBinding.this.subViewModelDelegate.destroyView(CollectionBinding.this.getViewBindingMeta().getViewModel(),
																					removedChildViewModel,
																					sourceIndex);
						}

						@Override
						public void onFailure(final Throwable t) {
							// TODO explanation
							LOG.error("",
									  t);
						}
					},
					this.dataModelExecutor);
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
		final ListenableFuture<?> futureChildView = this.subViewModelDelegate.newView(this.getViewBindingMeta().getViewModel(),
																					  this.observableCollection.view(),
																					  elementIndex);
		addCallback(futureChildView,
					new FutureCallback<Object>() {
						@Override
						public void onSuccess(final Object childViewModel) {
							CollectionBinding.this.viewBinder.bind(CollectionBinding.this.dataModelExecutor,
																   elementDataModel,
																   childViewModel);
							CollectionBinding.this.dataModelElementToViewModelElement.put(elementDataModel,
																						  childViewModel);
						}

						@Override
						public void onFailure(final Throwable t) {
							LOG.error("Error while creating new child viewModel.",
									  t);
						}
					},
					this.dataModelExecutor);
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

				final Object changedChildViewModel = this.dataModelElementToViewModelElement.get(childDataModel);

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

			final Object changedChildViewModel = this.dataModelElementToViewModelElement.remove(oldChildDataModel);
			this.dataModelElementToViewModelElement.put(newChildDataModel,
														changedChildViewModel);

			this.viewBinder.unbind(this.dataModelExecutor,
								   oldChildDataModel,
								   changedChildViewModel);
			this.viewBinder.bind(this.dataModelExecutor,
								 newChildDataModel,
								 changedChildViewModel);
		}
	}
}