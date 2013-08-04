/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/

package org.trinity.foundation.api.render.binding;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.util.concurrent.Futures.addCallback;
import static java.lang.String.format;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.onami.autobind.annotations.Bind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.render.binding.view.DataContext;
import org.trinity.foundation.api.render.binding.view.EventSignal;
import org.trinity.foundation.api.render.binding.view.EventSignalFilter;
import org.trinity.foundation.api.render.binding.view.InputSignals;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.PropertyAdapter;
import org.trinity.foundation.api.render.binding.view.PropertySlot;
import org.trinity.foundation.api.render.binding.view.PropertySlots;
import org.trinity.foundation.api.render.binding.view.ViewElementTypes;
import org.trinity.foundation.api.render.binding.view.delegate.ChildViewDelegate;
import org.trinity.foundation.api.render.binding.view.delegate.PropertySlotInvocatorDelegate;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

import com.google.common.base.CaseFormat;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Bind
@Singleton
@NotThreadSafe
public class BinderImpl implements Binder {

	private static final Logger LOG = LoggerFactory.getLogger(BinderImpl.class);
	private static final Cache<Class<?>, Cache<String, Optional<Method>>> GETTER_CACHE = CacheBuilder.newBuilder()
			.build();
	private static final Cache<Class<?>, Field[]> CHILD_VIEW_CACHE = CacheBuilder.newBuilder().build();
	private static final String GET_BOOLEAN_PREFIX = "is";
	private static final String GET_PREFIX = "get";
	private final PropertySlotInvocatorDelegate propertySlotDelegate;
	private final Injector injector;
	private final ChildViewDelegate childViewDelegate;
	private final ViewElementTypes viewElementTypes;
	private final Map<Object, Object> dataContextValueByView = new WeakHashMap<Object, Object>();
	private final Map<Object, Set<Object>> viewsByDataContextValue = new WeakHashMap<Object, Set<Object>>();
	private final Map<Object, PropertySlots> propertySlotsByView = new WeakHashMap<Object, PropertySlots>();
	private final Map<Object, ObservableCollection> observableCollectionByView = new WeakHashMap<Object, ObservableCollection>();
	private final Map<Object, InputSignals> inputSignalsByView = new WeakHashMap<Object, InputSignals>();
	private final Map<Object, Map<Object, DataContext>> dataContextByViewByParentDataContextValue = new WeakHashMap<Object, Map<Object, DataContext>>();

	@Inject
	BinderImpl(	final Injector injector,
				final PropertySlotInvocatorDelegate propertySlotInvocatorDelegate,
				final ChildViewDelegate childViewDelegate,
				final ViewElementTypes viewElementTypes) {
		this.injector = injector;
		this.childViewDelegate = childViewDelegate;
		this.propertySlotDelegate = propertySlotInvocatorDelegate;
		this.viewElementTypes = viewElementTypes;
	}

	@Override
	public void bind(	final Object model,
						final Object view) {
		LOG.debug(	"Bind model={} to view={}",
					model,
					view);

		checkNotNull(model);
		checkNotNull(view);

		bindViewElement(model,
						view,
						Optional.<DataContext> absent(),
						Optional.<InputSignals> absent(),
						Optional.<ObservableCollection> absent(),
						Optional.<PropertySlots> absent());
	}

	@Override
	public void updateBinding(	final Object model,
								final String propertyName) {
		checkNotNull(model);
		checkNotNull(propertyName);

		LOG.debug(	"Update binding for model={} of property={}",
					model,
					propertyName);

		updateDataContextBinding(	model,
									propertyName);
		updateProperties(	model,
							propertyName);
	}

	protected void updateDataContextBinding(final Object model,
											final String propertyName) {
		checkNotNull(model);
		checkNotNull(propertyName);

		final Map<Object, DataContext> dataContextByView = this.dataContextByViewByParentDataContextValue.get(model);
		if (dataContextByView == null) {
			return;
		}
		for (final Entry<Object, DataContext> dataContextByViewEntry : dataContextByView.entrySet()) {

			final Object view = dataContextByViewEntry.getKey();
			final DataContext dataContext = dataContextByViewEntry.getValue();
			final Optional<DataContext> optionalDataContext = Optional.of(dataContext);
			final Optional<InputSignals> optionalInputSignals = Optional
					.fromNullable(this.inputSignalsByView.get(view));
			final Optional<ObservableCollection> optionalObservableCollection = Optional
					.fromNullable(this.observableCollectionByView.get(view));
			final Optional<PropertySlots> optionalPropertySlots = Optional.fromNullable(this.propertySlotsByView
					.get(view));

			if (dataContext.value().startsWith(propertyName)) {

				bindViewElement(model,
								view,
								optionalDataContext,
								optionalInputSignals,
								optionalObservableCollection,
								optionalPropertySlots);
			}
		}
	}

	protected void updateProperties(final Object model,
									final String propertyName) {
		checkNotNull(model);
		checkNotNull(propertyName);

		try {
			final Optional<Method> optionalGetter = findGetter(	model.getClass(),
																propertyName);
			if (!optionalGetter.isPresent()) {
				return;
			}
			final Object propertyValue = optionalGetter.get().invoke(model);
			final Set<Object> views = this.viewsByDataContextValue.get(model);
			if (views == null) {
				return;
			}
			for (final Object view : views) {
				final PropertySlots propertySlots = this.propertySlotsByView.get(view);
				if (propertySlots == null) {
					continue;
				}
				for (final PropertySlot propertySlot : propertySlots.value()) {
					final String propertySlotPropertyName = propertySlot.propertyName();
					if (propertySlotPropertyName.equals(propertyName)) {
						invokePropertySlot(	view,
											propertySlot,
											propertyValue);
					}
				}
			}

		} catch (final IllegalAccessException e) {
			// TODO explanation
			LOG.error(	"",
						e);
		} catch (final IllegalArgumentException e) {
			// TODO explanation
			LOG.error(	"",
						e);
		} catch (final InvocationTargetException e) {
			// TODO explanation
			LOG.error(	"",
						e);
		} catch (final ExecutionException e) {
			// TODO explanation
			LOG.error(	"",
						e);
		}
	}

	protected void bindViewElement(	final Object inheritedDataContext,
									final Object view,
									final Optional<DataContext> optionalFieldLevelDataContext,
									final Optional<InputSignals> optionalFieldLEvelInputSignals,
									final Optional<ObservableCollection> optionalFieldLevelObservableCollection,
									final Optional<PropertySlots> optionalFieldLevelPropertySlots) {
		checkNotNull(inheritedDataContext);
		checkNotNull(view);

		final Class<?> viewClass = view.getClass();

		// check for class level annotations if field level annotations are
		// absent
		final Optional<DataContext> optionalDataContext = optionalFieldLevelDataContext.or(Optional
				.<DataContext> fromNullable(viewClass.getAnnotation(DataContext.class)));
		final Optional<InputSignals> optionalInputSignals = optionalFieldLEvelInputSignals.or(Optional
				.<InputSignals> fromNullable(viewClass.getAnnotation(InputSignals.class)));
		final Optional<ObservableCollection> optionalObservableCollection = optionalFieldLevelObservableCollection
				.or(Optional.<ObservableCollection> fromNullable(viewClass.getAnnotation(ObservableCollection.class)));
		final Optional<PropertySlots> optionalPropertySlots = optionalFieldLevelPropertySlots.or(Optional
				.<PropertySlots> fromNullable(viewClass.getAnnotation(PropertySlots.class)));

		Object dataContext = inheritedDataContext;
		if (optionalDataContext.isPresent()) {
			final Optional<Object> optionalDataContextValue = getDataContextValueForView(	dataContext,
																							view,
																							optionalDataContext.get());
			if (optionalDataContextValue.isPresent()) {
				dataContext = optionalDataContextValue.get();
			} else {
				return;
			}
		}
		registerBinding(dataContext,
						view);

		if (optionalInputSignals.isPresent()) {
			final EventSignal[] eventSignals = optionalInputSignals.get().value();
			bindEventSignals(dataContext,
					view,
					eventSignals);
		}

		if (optionalObservableCollection.isPresent()) {
			final ObservableCollection observableCollection = optionalObservableCollection.get();
			bindObservableCollection(	dataContext,
										view,
										observableCollection);
		}

		if (optionalPropertySlots.isPresent()) {
			final PropertySlots propertySlots = optionalPropertySlots.get();
			bindPropertySlots(	dataContext,
								view,
								propertySlots);
		}

		bindChildViewElements(	dataContext,
								view);
	}

	protected void bindObservableCollection(final Object dataContext,
											final Object view,
											final ObservableCollection observableCollection) {
		checkNotNull(dataContext);
		checkNotNull(view);
		checkNotNull(observableCollection);

		try {
			final String collectionProperty = observableCollection.value();

			final Optional<Method> collectionGetter = findGetter(	dataContext.getClass(),
																	collectionProperty);
			if (!collectionGetter.isPresent()) {
				return;
			}

			final Object collection = collectionGetter.get().invoke(dataContext);

			checkArgument(	collection instanceof EventList,
							format(	"Observable collection must be bound to a property of type %s @ dataContext: %s, view: %s, observable collection: %s",
									EventList.class.getName(),
									dataContext,
									view,
									observableCollection));

			final EventList<?> contextCollection = (EventList<?>) collection;
			final Class<?> childViewClass = observableCollection.view();

			try {
				contextCollection.getReadWriteLock().readLock().lock();

				for (int i = 0; i < contextCollection.size(); i++) {
					final Object childViewDataContext = contextCollection.get(i);
					final ListenableFuture<?> futureChildView = this.childViewDelegate.newView(	view,
																								childViewClass,
																								i);
					addCallback(futureChildView,
								new FutureCallback<Object>() {
									@Override
									public void onSuccess(final Object childView) {
										bind(	childViewDataContext,
												childView);

									}

									@Override
									public void onFailure(final Throwable t) {
										LOG.error(	"Error while creating new child view.",
													t);
									}
								});
				}

				contextCollection.addListEventListener(new ListEventListener<Object>() {

					// We use a shadow list because glazedlists does not
					// give us the deleted object...
					private final List<Object> shadowChildDataContextList = new ArrayList<Object>(contextCollection);

					@Override
					public void listChanged(final ListEvent<Object> listChanges) {
						handleListChanged(	view,
											childViewClass,
											this.shadowChildDataContextList,
											listChanges);
					}
				});

			} finally {
				contextCollection.getReadWriteLock().readLock().unlock();
			}

		} catch (final IllegalAccessException e) {
			// TODO explanation
			LOG.error(	"",
						e);
		} catch (final IllegalArgumentException e) {
			// TODO explanation
			LOG.error(	"",
						e);
		} catch (final InvocationTargetException e) {
			// TODO explanation
			LOG.error(	"",
						e);
		} catch (final ExecutionException e) {
			// TODO explanation
			LOG.error(	"",
						e);
		}
	}

	protected void handleListChanged(	final Object view,
										final Class<?> childViewClass,
										final List<Object> shadowChildDataContextList,
										final ListEvent<Object> listChanges) {
		while (listChanges.next()) {
			final int sourceIndex = listChanges.getIndex();
			final int changeType = listChanges.getType();
			final List<Object> changeList = listChanges.getSourceList();

			switch (changeType) {
				case ListEvent.DELETE: {
					final Object removedObject = shadowChildDataContextList.remove(sourceIndex);
					checkNotNull(removedObject);

					final Set<Object> removedChildViews = BinderImpl.this.viewsByDataContextValue.get(removedObject);
					for (final Object removedChildView : removedChildViews) {
						BinderImpl.this.childViewDelegate.destroyView(	view,
																		removedChildView,
																		sourceIndex);
					}

					break;
				}
				case ListEvent.INSERT: {
					final Object childViewDataContext = changeList.get(sourceIndex);
					checkNotNull(childViewDataContext);

					shadowChildDataContextList.add(	sourceIndex,
													childViewDataContext);

					final ListenableFuture<?> futureChildView = this.childViewDelegate.newView(	view,
																								childViewClass,
																								sourceIndex);
					addCallback(futureChildView,
								new FutureCallback<Object>() {
									@Override
									public void onSuccess(final Object childView) {
										bind(	childViewDataContext,
												childView);

									}

									@Override
									public void onFailure(final Throwable t) {
										LOG.error(	"Error while creating new child view.",
													t);
									}
								});
					break;
				}
				case ListEvent.UPDATE: {
					if (listChanges.isReordering()) {
						final int[] reorderings = listChanges.getReorderMap();
						for (int i = 0; i < reorderings.length; i++) {
							final int newPosition = reorderings[i];
							final Object childViewDataContext = changeList.get(sourceIndex);

							shadowChildDataContextList.clear();
							shadowChildDataContextList.add(	newPosition,
															childViewDataContext);

							final Set<Object> changedChildViews = BinderImpl.this.viewsByDataContextValue
									.get(childViewDataContext);
							for (final Object changedChildView : changedChildViews) {
								BinderImpl.this.childViewDelegate.updateChildViewPosition(	view,
																							changedChildView,
																							i,
																							newPosition);
							}
						}
					} else {
						final Object newChildViewDataContext = changeList.get(sourceIndex);
						final Object oldChildViewDataContext = shadowChildDataContextList
								.set(	sourceIndex,
										newChildViewDataContext);
						checkNotNull(oldChildViewDataContext);
						checkNotNull(newChildViewDataContext);

						final Object childView = BinderImpl.this.viewsByDataContextValue.get(oldChildViewDataContext);

						bind(	newChildViewDataContext,
								childView);
					}

					break;
				}
			}
		}
	}

	protected void bindEventSignals(final Object dataContext,
	                                final Object view,
	                                final EventSignal[] eventSignals) {
		checkNotNull(dataContext);
		checkNotNull(view);
		checkNotNull(eventSignals);

		for (final EventSignal eventSignal : eventSignals) {
			final Class<? extends EventSignalFilter> eventSignalFilterType = eventSignal.filter();
			final String inputSlotName = eventSignal.name();

			//FIXME cache filter & uninstall any previous filter installments
			final EventSignalFilter eventSignalFilter = this.injector.getInstance(eventSignalFilterType);
			eventSignalFilter.installFilter(view,
											new SignalImpl(	dataContext,
															inputSlotName));

		}
	}

	protected void registerBinding(	final Object dataContext,
									final Object view) {
		checkNotNull(dataContext);
		checkNotNull(view);

		final Object oldDataContext = this.dataContextValueByView.put(	view,
																		dataContext);
		if (oldDataContext != null) {
			final Set<Object> oldDataContextViews = this.viewsByDataContextValue.get(oldDataContext);
			if (oldDataContextViews != null) {
				oldDataContextViews.remove(view);
			}
		}
		Set<Object> dataContextViews = this.viewsByDataContextValue.get(dataContext);
		if (dataContextViews == null) {
			dataContextViews = Sets.newSetFromMap(new WeakHashMap<Object, Boolean>());
			this.viewsByDataContextValue.put(	dataContext,
												dataContextViews);
		}
		dataContextViews.add(view);
	}

	protected void bindPropertySlots(	final Object dataContext,
										final Object view,
										final PropertySlots propertySlots) {
		checkNotNull(dataContext);
		checkNotNull(view);
		checkNotNull(propertySlots);

		this.propertySlotsByView.put(	view,
										propertySlots);
		for (final PropertySlot propertySlot : propertySlots.value()) {
			bindPropertySlot(	dataContext,
								view,
								propertySlot);
		}
	}

	protected void bindPropertySlot(final Object dataContext,
									final Object view,
									final PropertySlot propertySlot) {
		checkNotNull(dataContext);
		checkNotNull(view);
		checkNotNull(propertySlot);

		try {
			final String propertySlotDataContext = propertySlot.dataContext();
			Object propertyDataContext;
			if (propertySlotDataContext.isEmpty()) {
				propertyDataContext = dataContext;
			} else {
				final Optional<Object> optionalRelativeDataContext = getDataContextValue(	dataContext,
																							propertySlotDataContext);
				if (optionalRelativeDataContext.isPresent()) {
					propertyDataContext = optionalRelativeDataContext.get();
				} else {
					return;
				}
			}
			final String propertyName = propertySlot.propertyName();
			final Optional<Method> optionalGetter = findGetter(	propertyDataContext.getClass(),
																propertyName);
			if (optionalGetter.isPresent()) {
				final Method getter = optionalGetter.get();

				// workaround for bug (4071957) submitted in
				// 1997(!) and still not fixed by sun/oracle.
				if (propertyDataContext.getClass().isAnonymousClass()) {
					getter.setAccessible(true);
				}

				final Object propertyInstance = optionalGetter.get().invoke(propertyDataContext);

				invokePropertySlot(	view,
									propertySlot,
									propertyInstance);

			}
		} catch (final IllegalAccessException e) {
			// TODO explanation
			LOG.error(	"",
						e);
		} catch (final IllegalArgumentException e) {
			// TODO explanation
			LOG.error(	"",
						e);
		} catch (final InvocationTargetException e) {
			// TODO explanation
			LOG.error(	"",
						e);
		} catch (final ExecutionException e) {
			// TODO explanation
			LOG.error(	"",
						e);
		}

	}

	protected void invokePropertySlot(	final Object view,
										final PropertySlot propertySlot,
										final Object propertyValue) {
		checkNotNull(view);
		checkNotNull(propertySlot);
		checkNotNull(propertyValue);

		try {
			final String viewMethodName = propertySlot.methodName();
			final Class<?>[] viewMethodArgumentTypes = propertySlot.argumentTypes();
			final Method targetViewMethod = view.getClass().getMethod(	viewMethodName,
																		viewMethodArgumentTypes);
			final Class<? extends PropertyAdapter<?>> propertyAdapterType = propertySlot.adapter();
			@SuppressWarnings("rawtypes")
			final PropertyAdapter propertyAdapter = propertyAdapterType.newInstance();
			@SuppressWarnings("unchecked")
			final Object argument = propertyAdapter.adapt(propertyValue);

			this.propertySlotDelegate.invoke(	view,
												targetViewMethod,
												argument);
		} catch (final NoSuchMethodException e) {
			// TODO explanation
			LOG.error(	"",
						e);
		} catch (final SecurityException e) {
			// TODO explanation
			LOG.error(	"",
						e);
		} catch (final InstantiationException e) {
			// TODO explanation
			LOG.error(	"",
						e);
		} catch (final IllegalAccessException e) {
			// TODO explanation
			LOG.error(	"",
						e);
		}
	}

	protected Optional<Object> getDataContextValueForView(	final Object parentDataContextValue,
															final Object view,
															final DataContext dataContext) {
		checkNotNull(parentDataContextValue);
		checkNotNull(view);
		checkNotNull(dataContext);

		Map<Object, DataContext> dataContextByView = this.dataContextByViewByParentDataContextValue
				.get(parentDataContextValue);
		if (dataContextByView == null) {
			dataContextByView = new WeakHashMap<Object, DataContext>();
			this.dataContextByViewByParentDataContextValue.put(	parentDataContextValue,
																dataContextByView);
		}
		dataContextByView.put(	view,
								dataContext);

		final String propertyChain = dataContext.value();
		return getDataContextValue(	parentDataContextValue,
									propertyChain);
	}

	protected Field[] findChildViews(final Class<?> modelClass) throws ExecutionException {
		return CHILD_VIEW_CACHE.get(modelClass,
									new Callable<Field[]>() {
										@Override
										public Field[] call() throws Exception {
											final List<Field> possibleChildViews = new ArrayList<Field>();
											final Field[] childViewElements = modelClass.getDeclaredFields();
											for (final Field childViewElement : childViewElements) {
												// filter out types we're
												// definitely not interested in
												boolean interestedViewElement = false;
												for (final Class<?> validViewElementType : BinderImpl.this.viewElementTypes
														.getViewElementTypes()) {
													interestedViewElement |= validViewElementType
															.isAssignableFrom(childViewElement.getDeclaringClass());

													if (interestedViewElement) {
														possibleChildViews.add(childViewElement);
														break;
													}
												}
											}

											return possibleChildViews.toArray(new Field[] {});
										}
									});
	}

	protected void bindChildViewElements(	final Object inheritedModel,
											final Object view) {
		checkNotNull(inheritedModel);
		checkNotNull(view);

		try {

			final Class<?> viewClass = view.getClass();

			// TODO Cache field lookup so we only get fields we're potentially
			// interested in.
			final Field[] childViewElements = viewClass.getDeclaredFields();

			for (final Field childViewElement : childViewElements) {

				childViewElement.setAccessible(true);
				final Object childView = childViewElement.get(view);
				childViewElement.setAccessible(false);

				// filter out null values
				if (childView == null) {
					continue;
				}

				// recursion safety
				if (this.dataContextValueByView.containsKey(childView)) {
					continue;
				}

				final Optional<DataContext> optionalFieldDataContext = Optional
						.<DataContext> fromNullable(childViewElement.getAnnotation(DataContext.class));
				final Optional<InputSignals> optionalFieldInputSignals = Optional
						.<InputSignals> fromNullable(childViewElement.getAnnotation(InputSignals.class));
				final Optional<ObservableCollection> optionalFieldObservableCollection = Optional
						.<ObservableCollection> fromNullable(childViewElement.getAnnotation(ObservableCollection.class));
				final Optional<PropertySlots> optionalFieldPropertySlots = Optional
						.<PropertySlots> fromNullable(childViewElement.getAnnotation(PropertySlots.class));

				bindViewElement(inheritedModel,
								childView,
								optionalFieldDataContext,
								optionalFieldInputSignals,
								optionalFieldObservableCollection,
								optionalFieldPropertySlots);

			}
		} catch (final IllegalArgumentException e) {
			// TODO explanation
			LOG.error(	"",
						e);
		} catch (final IllegalAccessException e) {
			// TODO explanation
			LOG.error(	"",
						e);
		}
	}

	protected Iterable<String> toPropertyNames(final String subModelPath) {
		checkNotNull(subModelPath);

		return Splitter.on('.').trimResults().omitEmptyStrings().split(subModelPath);
	}

	protected Optional<Object> getDataContextValue(	final Object model,
													final String propertyChain) {
		checkNotNull(model);
		checkNotNull(propertyChain);

		final Iterable<String> propertyNames = toPropertyNames(propertyChain);

		Object currentModel = model;
		try {

			for (final String propertyName : propertyNames) {
				if (currentModel == null) {
					break;
				}
				final Class<?> currentModelClass = currentModel.getClass();
				final Optional<Method> foundMethod = findGetter(currentModelClass,
																propertyName);
				if (foundMethod.isPresent()) {
					currentModel = foundMethod.get().invoke(currentModel);
				}
			}
		} catch (final IllegalAccessException e) {
			LOG.error(	String.format(	"Can not access getter on %s. Is it a no argument public method?",
										currentModel),
						e);
		} catch (final IllegalArgumentException e) {
			// TODO explanation
			LOG.error(	"",
						e);
		} catch (final InvocationTargetException e) {
			// TODO explanation
			LOG.error(	"",
						e);
		} catch (final ExecutionException e) {
			Throwables.propagate(e);
		}
		return Optional.fromNullable(currentModel);
	}

	protected Optional<Method> findGetter(	final Class<?> modelClass,
											final String propertyName) throws ExecutionException {
		checkNotNull(modelClass);
		checkNotNull(propertyName);
		return getGetterMethod(	modelClass,
								propertyName);
	}

	protected Optional<Method> getGetterMethod(	final Class<?> modelClass,
												final String propertyName) throws ExecutionException {
		return GETTER_CACHE.get(modelClass,
								new Callable<Cache<String, Optional<Method>>>() {
									@Override
									public Cache<String, Optional<Method>> call() {

										return CacheBuilder.newBuilder().build();
									}
								}).get(	propertyName,
										new Callable<Optional<Method>>() {
											@Override
											public Optional<Method> call() {
												Method foundMethod = null;
												String getterMethodName = toGetterMethodName(propertyName);

												try {
													foundMethod = modelClass.getMethod(getterMethodName);
												} catch (final NoSuchMethodException e) {
													// no getter with get found,
													// try with is.
													getterMethodName = toBooleanGetterMethodName(propertyName);
													try {
														foundMethod = modelClass.getMethod(getterMethodName);
													} catch (final NoSuchMethodException e1) {
														// TODO explanation
														LOG.error(	"",
																	e1);

													} catch (final SecurityException e1) {
														LOG.error(	format(	"Property %s is not accessible on %s. Did you declare it as public?",
																			propertyName,
																			modelClass.getName()),
																	e);
													}
												} catch (final SecurityException e1) {
													// TODO explanation
													LOG.error(	format(	"Property %s is not accessible on %s. Did you declare it as public?",
																		propertyName,
																		modelClass.getName()),
																e1);
												}
												return Optional.fromNullable(foundMethod);
											}
										});
	}

	protected String toGetterMethodName(final String propertyName) {
		return toGetterMethodName(	GET_PREFIX,
									propertyName);
	}

	protected String toGetterMethodName(final String prefix,
										final String propertyName) {
		checkNotNull(prefix);
		checkNotNull(propertyName);

		return prefix + CaseFormat.LOWER_CAMEL.to(	CaseFormat.UPPER_CAMEL,
													propertyName);
	}

	protected String toBooleanGetterMethodName(final String propertyName) {
		return toGetterMethodName(	GET_BOOLEAN_PREFIX,
									propertyName);
	}
}
