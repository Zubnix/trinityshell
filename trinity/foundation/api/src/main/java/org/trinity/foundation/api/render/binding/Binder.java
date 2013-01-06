package org.trinity.foundation.api.render.binding;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.render.binding.view.DataContext;
import org.trinity.foundation.api.render.binding.view.InputSignal;
import org.trinity.foundation.api.render.binding.view.InputSignals;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.PropertyAdapter;
import org.trinity.foundation.api.render.binding.view.PropertySlot;
import org.trinity.foundation.api.render.binding.view.PropertySlots;
import org.trinity.foundation.api.render.binding.view.ViewElementTypes;
import org.trinity.foundation.api.render.binding.view.delegate.ChildViewDelegate;
import org.trinity.foundation.api.render.binding.view.delegate.InputListenerInstallerDelegate;
import org.trinity.foundation.api.render.binding.view.delegate.PropertySlotInvocatorDelegate;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

import com.google.common.base.CaseFormat;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class Binder {

	private static final String GET_BOOLEAN_PREFIX = "is";
	private static final String GET_PREFIX = "get";

	private final BindingAnnotationScanner bindingAnnotationScanner;
	private final PropertySlotInvocatorDelegate propertySlotDelegate;
	private final InputListenerInstallerDelegate inputListenerInstallerDelegate;
	private final ChildViewDelegate childViewDelegate;
	private final ViewElementTypes viewElementTypes;

	private final Map<Object, Object> dataContextValueByView = new WeakHashMap<Object, Object>();
	private final Map<Object, Set<Object>> viewsByDataContextValue = new WeakHashMap<Object, Set<Object>>();

	private final Map<Object, PropertySlots> propertySlotsByView = new WeakHashMap<Object, PropertySlots>();
	private final Map<Object, ObservableCollection> observableCollectionByView = new WeakHashMap<Object, ObservableCollection>();
	private final Map<Object, InputSignals> inputSignalsByView = new WeakHashMap<Object, InputSignals>();

	private final Map<Object, Map<Object, DataContext>> dataContextByViewByParentDataContextValue = new WeakHashMap<Object, Map<Object, DataContext>>();

	@Inject
	Binder(	final BindingAnnotationScanner bindingAnnotationScanner,
			final PropertySlotInvocatorDelegate propertySlotInvocatorDelegate,
			final InputListenerInstallerDelegate inputListenerInstallerDelegate,
			final ChildViewDelegate childViewDelegate,
			final ViewElementTypes viewElementTypes) {
		this.childViewDelegate = childViewDelegate;
		this.inputListenerInstallerDelegate = inputListenerInstallerDelegate;
		this.propertySlotDelegate = propertySlotInvocatorDelegate;
		this.bindingAnnotationScanner = bindingAnnotationScanner;
		this.viewElementTypes = viewElementTypes;
	}

	public void bind(	final Object model,
						final Object view) throws ExecutionException {

		bindViewElement(model,
						view,
						Optional.<DataContext> absent(),
						Optional.<InputSignals> absent(),
						Optional.<ObservableCollection> absent(),
						Optional.<PropertySlots> absent());
	}

	public void updateBinding(	final Object model,
								final String propertyName) throws ExecutionException {
		updateDataContextBinding(	model,
									propertyName);
		updateProperties(	model,
							propertyName);
	}

	protected void updateDataContextBinding(final Object model,
											final String propertyName) throws ExecutionException {

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
									final String propertyName) throws ExecutionException {
		try {
			final Object propertyValue = findGetter(model.getClass(),
													propertyName).invoke(model);
			final Set<Object> views = this.viewsByDataContextValue.get(model);
			if (views == null) {
				return;
			}
			for (final Object view : views) {
				final PropertySlots propertySlots = this.propertySlotsByView.get(view);
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
			throw new ExecutionException(e);
		} catch (final IllegalArgumentException e) {
			throw new ExecutionException(e);
		} catch (final InvocationTargetException e) {
			throw new ExecutionException(e);
		}

	}

	protected void bindViewElement(	final Object inheritedDataContext,
									final Object view,
									final Optional<DataContext> optionalFieldLevelDataContext,
									final Optional<InputSignals> optionalFieldLEvelInputSignals,
									final Optional<ObservableCollection> optionalFieldLevelObservableCollection,
									final Optional<PropertySlots> optionalFieldLevelPropertySlots)
			throws ExecutionException {
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
			final Optional<Object> optionalDataContextValue = getDataContextValue(	dataContext,
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
			final InputSignal[] inputSignals = optionalInputSignals.get().value();
			bindInputSignals(	dataContext,
								view,
								inputSignals);
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
											final ObservableCollection observableCollection) throws ExecutionException {
		checkArgument(dataContext instanceof EventList);

		final EventList<?> contextCollection = (EventList<?>) dataContext;
		final Class<?> childViewClass = observableCollection.value();

		for (final Object childViewDataContext : contextCollection) {
			final Object childView = this.childViewDelegate.newView(view,
																	childViewClass);
			bind(	childViewDataContext,
					childView);
		}

		contextCollection.addListEventListener(new ListEventListener<Object>() {
			@Override
			public void listChanged(final ListEvent<Object> listChanges) {
				// TODO update observableCollection

			}
		});
	}

	protected void bindInputSignals(final Object dataContext,
									final Object view,
									final InputSignal[] inputSignals) {
		for (final InputSignal inputSignal : inputSignals) {
			final Class<? extends Input> inputType = inputSignal.inputType();
			final String inputSlotName = inputSignal.name();

			this.inputListenerInstallerDelegate.installInputListener(	inputType,
																		view,
																		dataContext,
																		inputSlotName);
		}
	}

	protected void registerBinding(	final Object dataContext,
									final Object view) {
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
										final PropertySlots propertySlots) throws ExecutionException {
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
									final PropertySlot propertySlot) throws ExecutionException {

		try {
			final String propertyName = propertySlot.propertyName();
			final Object propertyInstance = findGetter(	dataContext.getClass(),
														propertyName).invoke(dataContext);
			invokePropertySlot(	view,
								propertySlot,
								propertyInstance);
		} catch (final IllegalAccessException e) {
			throw new ExecutionException(e);
		} catch (final IllegalArgumentException e) {
			throw new ExecutionException(e);
		} catch (final InvocationTargetException e) {
			throw new ExecutionException(e);
		}

	}

	protected void invokePropertySlot(	final Object view,
										final PropertySlot propertySlot,
										final Object propertyValue) throws ExecutionException {

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
			throw new ExecutionException(e);
		} catch (final SecurityException e) {
			throw new ExecutionException(e);
		} catch (final InstantiationException e) {
			throw new ExecutionException(e);
		} catch (final IllegalAccessException e) {
			throw new ExecutionException(e);
		}
	}

	protected Optional<Object> getDataContextValue(	final Object parentDataContextValue,
													final Object view,
													final DataContext dataContext) throws ExecutionException {
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
		final Iterable<String> propertyNames = toPropertyNames(propertyChain);

		final Object dataContextValue = getDataContextValue(parentDataContextValue,
															propertyNames);

		return Optional.fromNullable(dataContextValue);
	}

	protected void bindChildViewElements(	final Object inheritedModel,
											final Object view) throws ExecutionException {
		checkNotNull(inheritedModel);
		checkNotNull(view);

		try {

			final Class<?> viewClass = view.getClass();

			final Field[] childViewElements = viewClass.getDeclaredFields();

			for (final Field childViewElement : childViewElements) {

				childViewElement.setAccessible(true);
				final Object childView = childViewElement.get(view);
				childViewElement.setAccessible(false);

				// filter out null values
				if (childView == null) {
					continue;
				}

				// filter out types we're not interested in
				boolean interestedViewElement = false;
				for (final Class<?> viewElementType : this.viewElementTypes.getViewElementTypes()) {
					interestedViewElement |= viewElementType.isAssignableFrom(childView.getClass());
				}
				if (!interestedViewElement) {
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
			throw new ExecutionException(e);
		} catch (final IllegalAccessException e) {
			throw new ExecutionException(e);
		}
	}

	protected Iterable<String> toPropertyNames(final String subModelPath) {
		return Splitter.on('.').trimResults().omitEmptyStrings().split(subModelPath);
	}

	protected Optional<Object> getDataContextValue(	final Object model,
													final Iterable<String> propertyNames) throws ExecutionException {
		checkNotNull(model);

		Object currentModel = model;
		try {

			for (final String propertyName : propertyNames) {
				if (currentModel == null) {
					break;
				}
				final Class<?> currentModelClass = currentModel.getClass();
				final Method foundMethod = findGetter(	currentModelClass,
														propertyName);
				currentModel = foundMethod.invoke(currentModel);
			}
		} catch (final IllegalAccessException e) {
			throw new ExecutionException(	String.format(	"Can not access getter on %s. Is it a no argument public method?",
															currentModel),
											e);
		} catch (final IllegalArgumentException e) {
			throw new ExecutionException(e);
		} catch (final InvocationTargetException e) {
			throw new ExecutionException(e);
		}
		return Optional.fromNullable(currentModel);
	}

	protected Method findGetter(final Class<?> modelClass,
								final String propertyName) throws ExecutionException {
		Method foundMethod;
		String getterMethodName = toGetterMethodName(propertyName);
		try {
			foundMethod = this.bindingAnnotationScanner.lookupModelPropety(	modelClass,
																			getterMethodName);
		} catch (final ExecutionException e) {
			// no getter with get found, try with is. If nothing found, let
			// the exception bubble up.
			getterMethodName = toBooleanGetterMethodName(propertyName);
			foundMethod = this.bindingAnnotationScanner.lookupModelPropety(	modelClass,
																			getterMethodName);
		}
		return foundMethod;
	}

	protected String toGetterMethodName(final String propertyName) {
		return toGetterMethodName(	GET_PREFIX,
									propertyName);
	}

	protected String toGetterMethodName(final String prefix,
										final String propertyName) {
		return prefix + CaseFormat.LOWER_CAMEL.to(	CaseFormat.UPPER_CAMEL,
													propertyName);
	}

	protected String toBooleanGetterMethodName(final String propertyName) {
		return toGetterMethodName(	GET_BOOLEAN_PREFIX,
									propertyName);
	}
}
