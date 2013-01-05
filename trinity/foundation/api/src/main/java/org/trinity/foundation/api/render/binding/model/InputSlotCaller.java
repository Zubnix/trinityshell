package org.trinity.foundation.api.render.binding.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.display.input.InputModifiers;
import org.trinity.foundation.api.display.input.Keyboard;
import org.trinity.foundation.api.display.input.Momentum;
import org.trinity.foundation.api.render.binding.BindingAnnotationScanner;
import org.trinity.foundation.api.render.binding.view.BoundInputEvent;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind
@To(Type.IMPLEMENTATION)
@Singleton
public class InputSlotCaller {

	private final BindingAnnotationScanner bindingAnnotationScanner;
	private final Keyboard keyboard;

	@Inject
	InputSlotCaller(final BindingAnnotationScanner bindingAnnotationScanner, final Keyboard keyboard) {
		this.bindingAnnotationScanner = bindingAnnotationScanner;
		this.keyboard = keyboard;
	}

	public void notifyDataContextInputSlot(	final Object model,
											final BoundInputEvent boundInputEvent) {
		try {
			final Input input = boundInputEvent.getInput();
			final Optional<Method> optionalInputSlot = this.bindingAnnotationScanner
					.lookupInputSlot(	model.getClass(),
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
}
