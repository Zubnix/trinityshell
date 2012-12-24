package org.trinity.shellplugin.widget.api.binding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.display.api.event.DestroyNotifyEvent;
import org.trinity.foundation.display.api.event.InputNotifyEvent;
import org.trinity.foundation.input.api.Input;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Keyboard;
import org.trinity.foundation.input.api.Momentum;
import org.trinity.shell.api.widget.ShellWidget;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.eventbus.Subscribe;

/***************************************
 * Input listener for a {@link ShellWidget}. It listens for an
 * {@link InputNotifyEvent} and invokes the corresponding method of the
 * {@code ShellWidget} that was annotated with {@link InputSlot}.
 * 
 *************************************** 
 */
public class InputSignalDispatcher {

	private final BindingDiscovery bindingDiscovery;
	private final ShellWidget shellWidget;
	private final Keyboard keyboard;

	/***************************************
	 * Create a new {@code InputSignalDispatcher} and attach it to the given
	 * {@link ShellWidget}.
	 * 
	 * @param shellWidget
	 * @param bindingDiscovery
	 *************************************** 
	 */
	public InputSignalDispatcher(	final ShellWidget shellWidget,
									final BindingDiscovery bindingDiscovery,
									final Keyboard keyboard) {
		this.bindingDiscovery = bindingDiscovery;
		this.shellWidget = shellWidget;
		this.shellWidget.addShellNodeEventHandler(this);
		this.keyboard = keyboard;
	}

	@Subscribe
	public void handleBoundButtonInputEvent(final BoundInputEvent boundInputEvent) {
		try {
			final Input input = boundInputEvent.getInput();
			final Optional<Method> optionalInputSlot = this.bindingDiscovery
					.lookupInputSlot(	this.shellWidget.getClass(),
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
					optionalInputSlot.get().invoke(	this.shellWidget,
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

	@Subscribe
	public void handleDestroyNotify(final DestroyNotifyEvent destroyNotifyEvent) {
		this.shellWidget.removeShellNodeEventHandler(this);
	}
}