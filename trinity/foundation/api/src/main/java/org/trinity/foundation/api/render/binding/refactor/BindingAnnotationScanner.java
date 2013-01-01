package org.trinity.foundation.api.render.binding.refactor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.render.binding.refactor.model.InputSlot;
import org.trinity.foundation.api.render.binding.refactor.model.SubModel;
import org.trinity.foundation.api.render.binding.refactor.model.View;
import org.trinity.foundation.api.render.binding.refactor.view.InputSignals;
import org.trinity.foundation.api.render.binding.refactor.view.PropertySlot;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
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
	private final Cache<Class<?>, Method[]> inputSignals = CacheBuilder.newBuilder().build();
	private final Cache<Class<?>, Method[]> subModels = CacheBuilder.newBuilder().build();
	private final Cache<Class<?>, Method[]> propertySlots = CacheBuilder.newBuilder().build();

	@Inject
	BindingAnnotationScanner() {

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

	public Method[] lookupAllSubModels(final Class<?> viewClass) throws ExecutionException {
		return this.subModels.get(	viewClass,
									new Callable<Method[]>() {
										@Override
										public Method[] call() throws Exception {
											return getAllSubModels(viewClass);
										}
									});
	}

	public Method[] getAllSubModels(final Class<?> viewClass) {
		final List<Method> methods = new ArrayList<Method>();
		for (final Method viewMethod : viewClass.getMethods()) {
			SubModel subModel = viewMethod.getAnnotation(SubModel.class);
			if (subModel == null) {
				subModel = viewMethod.getReturnType().getAnnotation(SubModel.class);
			}
			if (subModel != null) {
				methods.add(viewMethod);
			}
		}
		return methods.toArray(new Method[] {});
	}

	public Method[] lookupAllInputSignals(final Class<?> viewClass) throws ExecutionException {
		return this.inputSignals.get(	viewClass,
										new Callable<Method[]>() {
											@Override
											public Method[] call() throws Exception {
												return getAllInputSignals(viewClass);
											}
										});
	}

	protected Method[] getAllInputSignals(final Class<?> viewClass) {

		final List<Method> methods = new ArrayList<Method>();
		for (final Method viewMethod : viewClass.getMethods()) {
			InputSignals inputSignals = viewMethod.getAnnotation(InputSignals.class);
			if (inputSignals == null) {
				inputSignals = viewMethod.getReturnType().getAnnotation(InputSignals.class);
			}
			if (inputSignals != null) {
				methods.add(viewMethod);
			}
		}

		return methods.toArray(new Method[] {});
	}

	public Method[] lookupAllPropertySlots(final Class<?> viewClass) throws ExecutionException {
		return this.propertySlots.get(	viewClass,
										new Callable<Method[]>() {
											@Override
											public Method[] call() throws Exception {
												return getAllPropertySlots(viewClass);
											}
										});
	}

	protected Method[] getAllPropertySlots(final Class<?> viewClass) {
		final List<Method> methods = new ArrayList<Method>();
		for (final Method viewMethod : viewClass.getMethods()) {
			PropertySlot propertySlot = viewMethod.getAnnotation(PropertySlot.class);
			if (propertySlot == null) {
				propertySlot = viewMethod.getReturnType().getAnnotation(PropertySlot.class);
			}
			if (propertySlot != null) {
				methods.add(viewMethod);
			}
		}
		return methods.toArray(new Method[] {});
	}
}
