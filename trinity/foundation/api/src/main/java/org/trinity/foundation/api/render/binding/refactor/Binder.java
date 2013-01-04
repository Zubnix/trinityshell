package org.trinity.foundation.api.render.binding.refactor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.render.binding.refactor.view.ChildViewHandler;
import org.trinity.foundation.api.render.binding.refactor.view.InputListenerInstaller;
import org.trinity.foundation.api.render.binding.refactor.view.InputSignal;
import org.trinity.foundation.api.render.binding.refactor.view.InputSignals;
import org.trinity.foundation.api.render.binding.refactor.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.refactor.view.PropertyAdapter;
import org.trinity.foundation.api.render.binding.refactor.view.PropertySlot;
import org.trinity.foundation.api.render.binding.refactor.view.PropertySlotInvocator;
import org.trinity.foundation.api.render.binding.refactor.view.SubModel;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

import com.google.common.base.CaseFormat;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind
@To(Type.IMPLEMENTATION)
@Singleton
public class Binder {

	private static final String GET_BOOLEAN_PREFIX = "is";
	private static final String GET_PREFIX = "get";

	private final Map<Object, Set<Object>> viewElementsByValue = new WeakHashMap<Object, Set<Object>>();
	private final Map<Object, Object> valueByView = new WeakHashMap<Object, Object>();

	private final InputListenerInstaller inputListenerInstaller;
	private final BindingAnnotationScanner bindingAnnotationScanner;
	private final PropertySlotInvocator propertySlotInvocator;
	private final ChildViewHandler childViewHandler;

	@Inject
	Binder(	final BindingAnnotationScanner bindingAnnotationScanner,
			final InputListenerInstaller inputListenerInstaller,
			final ChildViewHandler childViewHandler,
			final PropertySlotInvocator propertySlotInvocator) {
		this.bindingAnnotationScanner = bindingAnnotationScanner;
		this.inputListenerInstaller = inputListenerInstaller;
		this.propertySlotInvocator = propertySlotInvocator;
		this.childViewHandler = childViewHandler;
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

	protected void bindAllSubModels(final Object model,
									final Object view) {
		final Class<?> viewClass = view.getClass();
		try {

			for (final Entry<SubModel, Method> viewMethod : this.bindingAnnotationScanner.lookupAllSubModels(viewClass)
					.entrySet()) {
				final SubModel subModel = viewMethod.getKey();
				final Object subModelInstance = getSubModelInstance(model,
																	subModel.value());
				final Object childViewElement = viewMethod.getValue().invoke(view);

				bind(	subModelInstance,
						childViewElement);
			}
		} catch (final SecurityException e) {
			Throwables.propagate(e);
		} catch (final IllegalAccessException e) {
			Throwables.propagate(e);
		} catch (final IllegalArgumentException e) {
			Throwables.propagate(e);
		} catch (final InvocationTargetException e) {
			Throwables.propagate(e);
		} catch (final ExecutionException e) {
			Throwables.propagate(e);
		}
	}

	public void refresh(final Object model,
						final String property,
						final Object value) {
		final Set<Object> views = getViewElements(model);
	}

	public void bind(	final Object model,
						final Object view) {
		// remove old binding
		removeBinding(view);
		addBinding(	model,
					view);
		bindAllSubModels(	model,
							view);

		bindInput(	model,
					view);
		bindAllChildInputs(	model,
							view);

		bindAllChildProperties(	model,
								view);
		bindAllCollections(	model,
							view);

	}

	protected void removeBinding(final Object view) {
		final Object oldValue = this.valueByView.containsKey(view);
		if (oldValue != null) {
			this.viewElementsByValue.get(oldValue).remove(view);
		}
		this.valueByView.remove(view);
	}

	protected Set<Object> getViewElements(final Object model) {
		Set<Object> bindings = this.viewElementsByValue.get(model);
		if (bindings == null) {
			return Collections.emptySet();
		}
		bindings = Collections.unmodifiableSet(bindings);
		return bindings;
	}

	protected void addBinding(	final Object value,
								final Object viewElement) {
		if (this.viewElementsByValue.containsKey(value)) {
			this.viewElementsByValue.get(value).add(viewElement);
		} else {
			final Set<Object> viewElements = Sets.newSetFromMap(new WeakHashMap<Object, Boolean>());
			viewElements.add(viewElement);
			this.viewElementsByValue.put(	value,
											viewElements);
		}
		this.valueByView.put(	viewElement,
								value);
	}

	protected Object getSubModelInstance(	final Object model,
											final String subModelPath) throws ExecutionException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		final Iterable<String> propertyNames = Splitter.on('.').trimResults().omitEmptyStrings().split(subModelPath);
		Object currentModel = model;
		for (final String propertyName : propertyNames) {
			final Class<?> currentModelClass = currentModel.getClass();
			final Method foundMethod = findGetter(	currentModelClass,
													propertyName);
			currentModel = foundMethod.invoke(currentModel);
		}
		return currentModel;
	}

	protected void bindAllChildInputs(	final Object model,
										final Object view) {
		final Class<?> viewClass = view.getClass();
		// final List<InputBinding> inputBindings = new
		// ArrayList<InputBinding>();
		try {
			for (final Entry<InputSignals, Method> viewMethod : this.bindingAnnotationScanner
					.lookupAllInputSignals(viewClass).entrySet()) {

				final Object viewElement = viewMethod.getValue().invoke(view);
				Object boundModel;
				// check if this view is assigned to a submodel
				final Object subModel = this.valueByView.get(viewElement);
				if (subModel != null) {
					boundModel = subModel;
				} else {
					boundModel = model;
				}

				final InputSignals inputSignals = viewMethod.getKey();
				bindInput(	boundModel,
							viewElement,
							inputSignals);
			}
		} catch (final ExecutionException e) {
			Throwables.propagate(e);
		} catch (final IllegalAccessException e) {
			Throwables.propagate(e);
		} catch (final IllegalArgumentException e) {
			Throwables.propagate(e);
		} catch (final InvocationTargetException e) {
			Throwables.propagate(e);
		}
	}

	protected void bindInput(	final Object model,
								final Object view) {
		final InputSignals inputSignals = view.getClass().getAnnotation(InputSignals.class);
		if (inputSignals != null) {
			bindInput(	model,
						view,
						inputSignals);
		}
	}

	protected void bindInput(	final Object model,
								final Object view,
								final InputSignals inputSignals) {

		final InputSignal[] inputSignalMetas = inputSignals.value();
		for (final InputSignal inputSignal : inputSignalMetas) {

			final Class<? extends Input> inputType = inputSignal.inputType();
			final String inputSlotName = inputSignal.name();

			this.inputListenerInstaller.installInputListener(	inputType,
																view,
																model,
																inputSlotName);
		}
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

	protected void bindAllCollections(	final Object model,
										final Object view) {
		final Class<?> viewClass = view.getClass();
		try {
			for (final Method viewMethod : this.bindingAnnotationScanner.lookupObservableCollections(viewClass)) {

				final ObservableCollection observableCollection = viewMethod.getAnnotation(ObservableCollection.class);

				final Collection<?> collectionView = (Collection<?>) viewMethod.invoke(view);

				Object boundModel;
				final Object subModel = this.valueByView.get(collectionView);
				if (subModel != null) {
					boundModel = subModel;
				} else {
					boundModel = model;
				}

				if (!(boundModel instanceof EventList)) {
					// TODO make a more descriptive exception
					throw new IllegalArgumentException(String.format(	"Only collections that implement the %s interface can be observed.",
																		EventList.class.getName()));
				}

				// TODO use a guice provider to instantiate view?
				final Class<?> viewElementType = observableCollection.value();
				final EventList<?> boundModelList = (EventList<?>) boundModel;

				boundModelList.addListEventListener(new ListEventListener<Object>() {
					@Override
					public void listChanged(final ListEvent<Object> listChanges) {
						// TODO handle listchanges

					}
				});

				bindCollectionElements(	view,
										boundModelList,
										viewElementType);

			}
		} catch (final IllegalAccessException e) {
			Throwables.propagate(e);
		} catch (final IllegalArgumentException e) {
			Throwables.propagate(e);
		} catch (final InvocationTargetException e) {
			Throwables.propagate(e);
		} catch (final ExecutionException e) {
			Throwables.propagate(e);
		} catch (final InstantiationException e) {
			Throwables.propagate(e);
		} catch (final SecurityException e) {
			Throwables.propagate(e);
		}
	}

	protected void bindCollectionElements(	final Object view,
											final Iterable<?> elements,
											final Class<?> elementView) throws InstantiationException,
			IllegalAccessException {
		for (final Object modelElement : elements) {
			final Object childViewElement = elementView.newInstance();
			this.childViewHandler.handleNewChildViewElement(view,
															childViewElement);
			bind(	modelElement,
					childViewElement);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void bindAllChildProperties(	final Object model,
											final Object view) {
		final Class<?> viewClass = view.getClass();
		try {
			for (final Entry<PropertySlot, Method> viewMethod : this.bindingAnnotationScanner
					.lookupAllPropertySlots(viewClass).entrySet()) {

				final Object viewElement = viewMethod.getValue().invoke(view);
				Object boundModel;
				// check if this view is assigned to a submodel
				final Object subModel = this.valueByView.get(viewElement);
				if (subModel != null) {
					boundModel = subModel;
				} else {
					boundModel = model;
				}

				final PropertySlot propertySlot = viewMethod.getAnnotation(PropertySlot.class);
				final String propertyName = propertySlot.propertyName();
				final Object propertyInstance = findGetter(	boundModel.getClass(),
															propertyName).invoke(boundModel);

				final String viewMethodName = propertySlot.methodName();
				final Class<?>[] viewMethodArgumentTypes = propertySlot.argumentTypes();
				final Method targetViewMethod = view.getClass().getMethod(	viewMethodName,
																			viewMethodArgumentTypes);

				final Class<? extends PropertyAdapter> propertyAdapterType = propertySlot.adapter();
				final PropertyAdapter propertyAdapter = propertyAdapterType.newInstance();
				final Object argument = propertyAdapter.adapt(propertyInstance);

				this.propertySlotInvocator.invoke(	view,
													targetViewMethod,
													argument);
			}
		} catch (final IllegalAccessException e) {
			Throwables.propagate(e);
		} catch (final IllegalArgumentException e) {
			Throwables.propagate(e);
		} catch (final InvocationTargetException e) {
			Throwables.propagate(e);
		} catch (final NoSuchMethodException e) {
			Throwables.propagate(e);
		} catch (final SecurityException e) {
			Throwables.propagate(e);
		} catch (final InstantiationException e) {
			Throwables.propagate(e);
		} catch (final ExecutionException e) {
			Throwables.propagate(e);
		}
	}
}