package org.trinity.foundation.api.render.binding;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.trinity.foundation.api.display.input.InputModifier;
import org.trinity.foundation.api.display.input.InputModifiers;
import org.trinity.foundation.api.display.input.Keyboard;
import org.trinity.foundation.api.display.input.Momentum;
import org.trinity.foundation.api.display.input.PointerInput;

public class InputSignalDispatcherTest {

	@Test
	public void test() {
		final DummyDataContext mockedDummyShellWidget = mock(DummyDataContext.class);
		final DummyDataContext dummyDataContext = new DummyDataContext(mockedDummyShellWidget);
		final Keyboard keyboard = mock(Keyboard.class);
		final InputModifiers inputModifiers = mock(InputModifiers.class);
		when(keyboard.modifiers(InputModifier.MOD_1,
								InputModifier.MOD_CTRL)).thenReturn(inputModifiers);
		final BindingDiscovery bindingDiscovery = new BindingDiscovery(	keyboard,
																		null);

		final PointerInput input = mock(PointerInput.class);
		when(input.getInputModifiers()).thenReturn(inputModifiers);
		when(input.getMomentum()).thenReturn(Momentum.STARTED);
		final BoundInputEvent boundInputEvent = mock(BoundInputEvent.class);
		when(boundInputEvent.getInput()).thenReturn(input);
		when(boundInputEvent.getInputSlotName()).thenReturn("onClick");

		bindingDiscovery.notifyDataContextInputSlot(dummyDataContext.getClass(),
													dummyDataContext,
													boundInputEvent);

		verify(	mockedDummyShellWidget,
				times(1)).onClick(input);
	}
}
