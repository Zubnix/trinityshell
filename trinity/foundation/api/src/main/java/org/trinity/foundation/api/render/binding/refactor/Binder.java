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
import org.trinity.foundation.api.render.binding.refactor.model.SubModel;
import org.trinity.foundation.api.render.binding.refactor.view.ChildViewElementHandler;
import org.trinity.foundation.api.render.binding.refactor.view.InputListenerInstaller;
import org.trinity.foundation.api.render.binding.refactor.view.InputSignal;
import org.trinity.foundation.api.render.binding.refactor.view.InputSignals;
import org.trinity.foundation.api.render.binding.refactor.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.refactor.view.PropertyAdapter;
import org.trinity.foundation.api.render.binding.refactor.view.PropertySlot;
import org.trinity.foundation.api.render.binding.refactor.view.PropertySlotInvocator;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

import com.google.common.base.CaseFormat;
import com.google.common.base.Optional;
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
	private final ChildViewElementHandler childViewElementHandler;

	@Inject
	Binder(	final BindingAnnotationScanner bindingAnnotationScanner,
			final InputListenerInstaller inputListenerInstaller,
			final ChildViewElementHandler childViewElementHandler,
			final ViewBindingFactory viewBindingFactory,
			final PropertySlotInvocator propertySlotInvocator) {
		this.bindingAnnotationScanner = bindingAnnotationScanner;
		this.inputListenerInstaller = inputListenerInstaller;
		this.viewBindingFactory = viewBindingFactory;
		this.propertySlotInvocator = propertySlotInvocator;
		this.childViewElementHandler = childViewElementHandler;
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

	public Optional<Set<Object>> getSubViews(final Object subModel) {
		Set<Object> subViews = this.subViewsBySubModel.get(subModel);
		if (subViews == null) {
			return Optional.absent();
		} else {
			subViews = Collections.unmodifiableSet(subViews);
			return Optional.of(subViews);
		}
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
											final SubModel subModel) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		final String subModelChain = subModel.value();
		final Iterable<String> getterNames = Splitter.on('.').trimResults().omitEmptyStrings().split(subModelChain);
		Object currentModel = model;
		for (final String getterName : getterNames) {
			String getMethodName = toGetterMethodName(getterName);
			// TODO we might want to cache method lookup somehow if performance
			// is lacking here...
			Method foundMethod;
			try {
				foundMethod = model.getClass().getMethod(getMethodName);
			} catch (final NoSuchMethodError e) {
				// no getter with get found, try with is.
				getMethodName = getMethodName.replaceFirst(	GET_PREFIX,
															GET_BOOLEAN_PREFIX);
				// if nothing found, let the exception bubble up.
				foundMethod = model.getClass().getMethod(getMethodName);
			}

			currentModel = foundMethod.invoke(currentModel);
		}
		final Method subModelMethod = model.getClass().getMethod(subModelChain);
		return subModelMethod.invoke(model);
	}

	protected String toGetterMethodName(final String propertyName) {
		return GET_PREFIX + CaseFormat.LOWER_CAMEL.to(	CaseFormat.UPPER_CAMEL,
														propertyName);
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
			this.childViewElementHandler.handleNewChildViewElement(	view,
																	childViewElement);
			new ViewBinding(this,
							childViewElement,
							modelElement);
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
				final String getterMethodName = toGetterMethodName(propertyName);
				final Object propertyInstance = boundModel.getClass().getMethod(getterMethodName).invoke(boundModel);

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