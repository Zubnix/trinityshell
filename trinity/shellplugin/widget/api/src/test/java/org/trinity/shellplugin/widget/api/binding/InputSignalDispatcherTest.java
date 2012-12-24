package org.trinity.shellplugin.widget.api.binding;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.trinity.foundation.input.api.InputModifier;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Keyboard;
import org.trinity.foundation.input.api.Momentum;
import org.trinity.foundation.input.api.PointerInput;

public class InputSignalDispatcherTest {

	@Test
	public void test() {
		final DummyShellWidget dummyShellWidget = new DummyShellWidget();
		final BindingDiscovery bindingDiscovery = new BindingDiscovery(null);
		final InputModifiers inputModifiers = mock(InputModifiers.class);
		final PointerInput input = mock(PointerInput.class);
		when(input.getInputModifiers()).thenReturn(inputModifiers);
		when(input.getMomentum()).thenReturn(Momentum.STARTED);
		final BoundInputEvent boundInputEvent = mock(BoundInputEvent.class);
		when(boundInputEvent.getInput()).thenReturn(input);
		when(boundInputEvent.getInputSlotName()).thenReturn("onClick");
		final Keyboard keyboard = mock(Keyboard.class);
		when(keyboard.modifiers(InputModifier.MOD_1,
								InputModifier.MOD_CTRL)).thenReturn(inputModifiers);

		final InputSignalDispatcher inputSignalDispatcher = new InputSignalDispatcher(	dummyShellWidget,
																						bindingDiscovery,
																						keyboard);
		inputSignalDispatcher.handleBoundButtonInputEvent(boundInputEvent);

	}
}
