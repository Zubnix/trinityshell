package org.trinity.foundation.api.render.binding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.display.input.InputModifiers;
import org.trinity.foundation.api.display.input.Keyboard;
import org.trinity.foundation.api.display.input.Momentum;
import org.trinity.foundation.api.render.binding.error.BindingError;
import org.trinity.foundation.api.render.binding.model.InputSlot;
import org.trinity.foundation.api.render.binding.model.InputSlotCaller;
import org.trinity.foundation.api.render.binding.view.delegate.BoundInputEvent;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class InputSlotCallerImpl implements InputSlotCaller {

	private final HashFunction hashFunction = Hashing.goodFastHash(16);

	private final Cache<Integer, Optional<Method>> inputSlotCache = CacheBuilder.newBuilder().build();

	private final Keyboard keyboard;

	@Inject
	InputSlotCallerImpl(final Keyboard keyboard) {
		this.keyboard = keyboard;
	}

	@Override
	public void callInputSlot(	final Object model,
								final BoundInputEvent boundInputEvent) {
		try {
			final Input input = boundInputEvent.getInput();
			final Optional<Method> optionalInputSlot = getInputSlot(model.getClass(),
																	input.getClass(),
																	boundInputEvent.getInputSlotName());
			if (optionalInputSlot.isPresent()) {
				final Method inputSlot = optionalInputSlot.get();
				final InputSlot inputSlotAnnotation = inputSlot.getAnnotation(InputSlot.class);

				final InputModifiers slotInputModifiers = this.keyboard.modifiers(inputSlotAnnotation.modifier());
				final Momentum[] slotMomenta = inputSlotAnnotation.momentum();
				final InputModifiers inputModifiers = input.getInputModifiers();
				final Momentum inputMomentum = input.getMomentum();

				final boolean validMomemtum = Arrays.asList(slotMomenta).contains(inputMomentum);
				final boolean validModifiers = slotInputModifiers.equals(inputModifiers);

				if (validMomemtum && validModifiers) {
					optionalInputSlot.get().invoke(	model,
													input);
				}
			}
		} catch (final IllegalAccessException e) {
			// TODO explanation
			throw new BindingError(	"",
									e);
		} catch (final IllegalArgumentException e) {
			// TODO explanation
			throw new BindingError(	"",
									e);
		} catch (final InvocationTargetException e) {
			// TODO explanation
			throw new BindingError(	"",
									e);
		}
	}

	private Optional<Method> getInputSlot(	final Class<?> modelClass,
											final Class<? extends Input> inputClass,
											final String inputSlotName) {
		int key = hashFunction.newHasher().putInt(modelClass.hashCode()).putInt(inputClass.hashCode())
				.putString(inputSlotName).hashCode();
		try {
			return inputSlotCache.get(	Integer.valueOf(key),
										new Callable<Optional<Method>>() {
											@Override
											public Optional<Method> call() throws Exception {
												return Optional.fromNullable(lookupInputSlot(	modelClass,
																								inputClass,
																								inputSlotName));
											}
										});
		} catch (ExecutionException e) {
			Throwables.propagate(e);
			return null;
		}
	}

	private Method lookupInputSlot(	final Class<?> modelClass,
									final Class<? extends Input> inputClass,
									final String inputSlotName) {

		try {
			return modelClass.getMethod(inputSlotName,
										inputClass);
		} catch (NoSuchMethodException e) {
			throw new BindingError(	String.format(	"No public inputslot method with name %s and parameter of type %s found on class %s",
													inputSlotName,
													inputClass.getName(),
													modelClass.getName()),
									e);
		} catch (SecurityException e) {
			throw new BindingError(	String.format(	"No public inputslot method with name %s and parameter of type %s found on class %s",
													inputSlotName,
													inputClass.getName(),
													modelClass.getName()),
									e);
		}

	}
}
