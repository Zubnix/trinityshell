package org.trinity.foundation.api.render.binding.refactor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.foundation.api.display.input.PointerInput;
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

	private final Map<Object, Set<Object>> subViewsBySubModel = new WeakHashMap<Object, Set<Object>>();
	private final Map<Object, Object> subModelBySubViewElement = new WeakHashMap<Object, Object>();

	private final InputListenerInstaller inputListenerInstaller;
	private final ViewBindingFactory viewBindingFactory;
	private final BindingAnnotationScanner bindingAnnotationScanner;
	private final PropertySlotInvocator propertySlotInvocator;
	private final ChildViewHandler childViewHandler;

	@Inject
	Binder(	final BindingAnnotationScanner bindingAnnotationScanner,
			final InputListenerInstaller inputListenerInstaller,
			final ChildViewHandler childViewHandler,
			final ViewBindingFactory viewBindingFactory,
			final PropertySlotInvocator propertySlotInvocator) {
		this.bindingAnnotationScanner = bindingAnnotationScanner;
		this.inputListenerInstaller = inputListenerInstaller;
		this.viewBindingFactory = viewBindingFactory;
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

	public void bindSubModels(	final Object model,
								final Object view) {
		final Class<?> viewClass = view.getClass();
		try {
			for (final Method viewMethod : this.bindingAnnotationScanner.lookupAllSubModels(viewClass)) {

				final Object subModelInstance = getSubModelInstance(model,
																	viewMethod.getAnnotation(SubModel.class));
				final Object childViewElement = viewMethod.invoke(view);

				// This will link any object (including observable
				// collections) directly to the underlying submodel
				cleanupOldSubmodelSubViewLink(childViewElement);
				linkSubViewToSubModel(	subModelInstance,
										childViewElement);
				final ViewBinding subViewBinding = this.viewBindingFactory.createViewBinding(	childViewElement,
																								subModelInstance);
				subViewBinding.bindSubModels();
				subViewBinding.bindInput();
				subViewBinding.bindProperty();
				subViewBinding.bindCollections();
			}
		} catch (final NoSuchMethodException e) {
			Throwables.propagate(e);
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

	protected void cleanupOldSubmodelSubViewLink(final Object subViewInstance) {
		final Object oldSubModelInstance = this.subModelBySubViewElement.get(subViewInstance);
		if (oldSubModelInstance != null) {
			this.subViewsBySubModel.get(oldSubModelInstance).remove(subViewInstance);
		}
	}

	public Set<Object> getSubViews(final Object subModel) {
		Set<Object> subViews = this.subViewsBySubModel.get(subModel);
		if (subViews == null) {
			return Collections.emptySet();
		}
		subViews = Collections.unmodifiableSet(subViews);
		return subViews;
	}

	protected void linkSubViewToSubModel(	final Object subModelInstance,
											final Object subViewInstance) {
		if (this.subViewsBySubModel.containsKey(subModelInstance)) {
			this.subViewsBySubModel.get(subModelInstance).add(subViewInstance);
		} else {
			final Set<Object> subViews = Sets.newSetFromMap(new WeakHashMap<Object, Boolean>());
			subViews.add(subViewInstance);
			this.subViewsBySubModel.put(subModelInstance,
										subViews);
		}
		this.subModelBySubViewElement.put(	subViewInstance,
											subModelInstance);
	}

	protected Object getSubModelInstance(	final Object model,
											final SubModel subModel) throws ExecutionException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		final String subModelChain = subModel.value();
		final Iterable<String> propertyNames = Splitter.on('.').trimResults().omitEmptyStrings().split(subModelChain);
		Object currentModel = model;
		for (final String propertyName : propertyNames) {
			final Class<?> currentModelClass = currentModel.getClass();
			final Method foundMethod = findGetter(	currentModelClass,
													propertyName);
			currentModel = foundMethod.invoke(currentModel);
		}
		return currentModel;
	}

	public void bindInputs(	final Object model,
							final Object view) {
		final Class<?> viewClass = view.getClass();
		try {
			for (final Method viewMethod : this.bindingAnnotationScanner.lookupAllInputSignals(viewClass)) {

				Object boundModel;
				// check if this view is assigned to a submodel
				final Object subModel = this.subModelBySubViewElement.get(view);
				if (subModel != null) {
					boundModel = subModel;
				} else {
					boundModel = model;
				}

				final InputSignal[] inputSignalMetas = viewMethod.getAnnotation(InputSignals.class).value();
				for (final InputSignal inputSignal : inputSignalMetas) {

					final Class<? extends Input> inputType = inputSignal.inputType();
					final String inputSlotName = inputSignal.name();

					if (KeyboardInput.class.isAssignableFrom(inputType)) {
						this.inputListenerInstaller.installKeyInputListener(view,
																			boundModel,
																			inputSlotName);
					}

					if (PointerInput.class.isAssignableFrom(inputType)) {
						this.inputListenerInstaller.installButtonInputListener(	view,
																				boundModel,
																				inputSlotName);
					}
				}
			}
		} catch (final ExecutionException e) {
			Throwables.propagate(e);
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

	public void bindCollections(final Object model,
								final Object view) {
		final Class<?> viewClass = view.getClass();
		try {
			for (final Method viewMethod : this.bindingAnnotationScanner.lookupObservableCollections(viewClass)) {
				final ObservableCollection observableCollection = viewMethod.getAnnotation(ObservableCollection.class);

				final Collection<?> collectionView = (Collection<?>) viewMethod.invoke(view);
				Object boundModel;

				final Object subModel = this.subModelBySubViewElement.get(collectionView);
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
			new ViewBinding(this,
							modelElement,
							childViewElement);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void bindProperties(	final Object model,
								final Object view) {
		final Class<?> viewClass = view.getClass();
		try {
			for (final Method viewMethod : this.bindingAnnotationScanner.lookupAllPropertySlots(viewClass)) {
				Object boundModel;
				// check if this view is assigned to a submodel
				final Object subModel = this.subModelBySubViewElement.get(view);
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