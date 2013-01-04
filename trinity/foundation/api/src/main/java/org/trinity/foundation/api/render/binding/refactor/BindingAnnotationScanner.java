package org.trinity.foundation.api.render.binding.refactor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.render.binding.refactor.model.InputSlot;
import org.trinity.foundation.api.render.binding.refactor.model.View;
import org.trinity.foundation.api.render.binding.refactor.view.InputSignals;
import org.trinity.foundation.api.render.binding.refactor.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.refactor.view.PropertySlot;
import org.trinity.foundation.api.render.binding.refactor.view.SubModel;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind
@To(value = Type.IMPLEMENTATION)
@Singleton
public class BindingAnnotationScanner {

	private final Cache<Class<?>, Optional<Method>> views = CacheBuilder.newBuilder().build();
	private final Cache<Class<?>, Cache<Class<? extends Input>, Cache<String, Optional<Method>>>> inputSlots = CacheBuilder
			.newBuilder().build();
	private final Cache<Class<?>, Map<InputSignals, Method>> inputSignals = CacheBuilder.newBuilder().build();
	private final Cache<Class<?>, Map<SubModel, Method>> subModels = CacheBuilder.newBuilder().build();
	private final Cache<Class<?>, Map<PropertySlot, Method>> propertySlots = CacheBuilder.newBuilder().build();
	private final Cache<Class<?>, Map<ObservableCollection, Method>> observableCollections = CacheBuilder.newBuilder()
			.build();
	private final Cache<Class<?>, Cache<String, Method>> modelPropertiesByName = CacheBuilder.newBuilder().build();

	BindingAnnotationScanner() {

	}

	public Method lookupModelPropety(	final Class<?> modelClass,
										final String getterMethodName) throws ExecutionException {
		return this.modelPropertiesByName.get(	modelClass,
												new Callable<Cache<String, Method>>() {
													@Override
													public Cache<String, Method> call() throws Exception {
														return CacheBuilder.newBuilder().build();
													}
												}).get(	getterMethodName,
														new Callable<Method>() {
															@Override
															public Method call() throws Exception {
																return getModelPropety(	modelClass,
																						getterMethodName);
															}
														});
	}

	protected Method getModelPropety(	final Class<?> modelClass,
										final String getterMethodName) throws NoSuchMethodException, SecurityException {
		return modelClass.getMethod(getterMethodName);
	}

	public Optional<Method> lookupView(final Class<?> modelClass) throws ExecutionException {

		return this.views.get(	modelClass,
								new Callable<Optional<Method>>() {
									@Override
									public Optional<Method> call() {
										return getView(modelClass);
									}
								});
	}

	protected Optional<Method> getView(final Class<?> clazz) {
		Method foundMethod = null;
		final Method[] methods = clazz.getMethods();
		for (final Method method : methods) {
			final View view = method.getAnnotation(View.class);
			if ((view != null) && (foundMethod != null)) {
				throw new IllegalArgumentException(String.format(	"Found multiple %s on %s",
																	View.class.getName(),
																	clazz.getName()));
			} else if (view != null) {
				foundMethod = method;
			}
		}

		return Optional.fromNullable(foundMethod);
	}

	public Optional<Method> lookupInputSlot(final Class<?> modelClass,
											final Class<? extends Input> inputType,
											final String inputSlotName) throws ExecutionException {
		return this.inputSlots.get(	modelClass,
									new Callable<Cache<Class<? extends Input>, Cache<String, Optional<Method>>>>() {
										@Override
										public Cache<Class<? extends Input>, Cache<String, Optional<Method>>> call() {
											return CacheBuilder.newBuilder().build();
										}
									}).get(	inputType,
											new Callable<Cache<String, Optional<Method>>>() {
												@Override
												public Cache<String, Optional<Method>> call() {
													return CacheBuilder.newBuilder().build();
												}
											}).get(	inputSlotName,
													new Callable<Optional<Method>>() {
														@Override
														public Optional<Method> call() throws Exception {
															return getInputSlot(modelClass,
																				inputType,
																				inputSlotName);
														}
													});
	}

	protected Optional<Method> getInputSlot(final Class<?> modelClass,
											final Class<? extends Input> inputType,
											final String inputSlotName) throws SecurityException {
		Method method;
		try {
			method = modelClass.getMethod(	inputSlotName,
											inputType);
			final InputSlot inputSlotAnnotation = method.getAnnotation(InputSlot.class);
			if (inputSlotAnnotation == null) {
				method = null;
			}
		} catch (final NoSuchMethodException e) {
			method = null;
		}

		return Optional.fromNullable(method);
	}

	public Map<SubModel, Method> lookupAllSubModels(final Class<?> viewClass) throws ExecutionException {
		return this.subModels.get(	viewClass,
									new Callable<Map<SubModel, Method>>() {
										@Override
										public Map<SubModel, Method> call() throws Exception {
											return getAllSubModels(viewClass);
										}
									});
	}

	protected Map<SubModel, Method> getAllSubModels(final Class<?> viewClass) {
		final Map<SubModel, Method> submodels = new HashMap<SubModel, Method>();
		for (final Method viewMethod : viewClass.getMethods()) {
			final SubModel subModel = getAnnotation(viewMethod,
													SubModel.class);
			if (subModel != null) {
				submodels.put(	subModel,
								viewMethod);
			}
		}

		return Collections.unmodifiableMap(submodels);
	}

	public Map<InputSignals, Method> lookupAllInputSignals(final Class<?> viewClass) throws ExecutionException {
		return this.inputSignals.get(	viewClass,
										new Callable<Map<InputSignals, Method>>() {
											@Override
											public Map<InputSignals, Method> call() throws Exception {
												return getAllInputSignals(viewClass);
											}
										});
	}

	protected Map<InputSignals, Method> getAllInputSignals(final Class<?> viewClass) {

		final Map<InputSignals, Method> inputSignalsMap = new HashMap<InputSignals, Method>();
		for (final Method viewMethod : viewClass.getMethods()) {
			final InputSignals inputSignals = getAnnotation(viewMethod,
															InputSignals.class);
			if (inputSignals != null) {
				inputSignalsMap.put(inputSignals,
									viewMethod);
			}
		}

		return Collections.unmodifiableMap(inputSignalsMap);
	}

	public Map<PropertySlot, Method> lookupAllPropertySlots(final Class<?> viewClass) throws ExecutionException {
		return this.propertySlots.get(	viewClass,
										new Callable<Map<PropertySlot, Method>>() {
											@Override
											public Map<PropertySlot, Method> call() throws Exception {
												return getAllPropertySlots(viewClass);
											}
										});
	}

	protected Map<PropertySlot, Method> getAllPropertySlots(final Class<?> viewClass) {
		final Map<PropertySlot, Method> propertySlots = new HashMap<PropertySlot, Method>();
		for (final Method viewMethod : viewClass.getMethods()) {
			final PropertySlot propertySlot = getAnnotation(viewMethod,
															PropertySlot.class);
			if (propertySlot != null) {
				propertySlots.put(	propertySlot,
									viewMethod);
			}
		}
		return Collections.unmodifiableMap(propertySlots);
	}

	protected <T extends Annotation> T getAnnotation(	final Method method,
														final Class<T> annotationClass) {
		T annotation = method.getAnnotation(annotationClass);
		if (annotation == null) {
			annotation = method.getReturnType().getAnnotation(annotationClass);
		}

		return annotation;
	}

	public Map<ObservableCollection, Method> lookupObservableCollections(final Class<?> viewClass)
			throws ExecutionException {

		return this.observableCollections.get(	viewClass,
												new Callable<Map<ObservableCollection, Method>>() {
													@Override
													public Map<ObservableCollection, Method> call() {
														return getObservableCollections(viewClass);
													}
												});
	}

	protected Map<ObservableCollection, Method> getObservableCollections(final Class<?> viewClass) {
		final Map<ObservableCollection, Method> observableCollections = new HashMap<ObservableCollection, Method>();
		for (final Method viewMethod : viewClass.getMethods()) {
			final ObservableCollection observableCollection = getAnnotation(viewMethod,
																			ObservableCollection.class);
			if (observableCollection != null) {
				observableCollections.put(	observableCollection,
											viewMethod);
			}
		}
		return Collections.unmodifiableMap(observableCollections);
	}
}