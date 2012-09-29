package org.trinity.shell.impl.input;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.trinity.foundation.display.api.event.KeyNotifyEvent;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Key;
import org.trinity.foundation.input.api.Keyboard;
import org.trinity.foundation.input.api.KeyboardInput;

public class KeyInputStringBuilderImplTest {

	@Test
	public void test() {
		final InputModifiers inputModifiers = mock(InputModifiers.class);

		final Key d = mock(Key.class);
		final KeyboardInput dKeyboardInput = mock(KeyboardInput.class);
		when(dKeyboardInput.getKey()).thenReturn(d);
		when(dKeyboardInput.getInputModifiers()).thenReturn(inputModifiers);
		final KeyNotifyEvent dInput = mock(KeyNotifyEvent.class);
		when(dInput.getInput()).thenReturn(dKeyboardInput);

		final Key alt = mock(Key.class);
		final KeyboardInput altKeyboardInput = mock(KeyboardInput.class);
		when(altKeyboardInput.getKey()).thenReturn(alt);
		when(altKeyboardInput.getInputModifiers()).thenReturn(inputModifiers);
		final KeyNotifyEvent altInput = mock(KeyNotifyEvent.class);
		when(altInput.getInput()).thenReturn(altKeyboardInput);

		final Key backspace = mock(Key.class);
		final KeyboardInput backspaceKeyboardInput = mock(KeyboardInput.class);
		when(backspaceKeyboardInput.getKey()).thenReturn(backspace);
		when(backspaceKeyboardInput.getInputModifiers()).thenReturn(inputModifiers);
		final KeyNotifyEvent backspaceInput = mock(KeyNotifyEvent.class);
		when(backspaceInput.getInput()).thenReturn(backspaceKeyboardInput);

		final Keyboard keyboard = mock(Keyboard.class);
		when(keyboard.asKeySymbolName(	d,
										inputModifiers)).thenReturn("d");
		when(keyboard.asKeySymbolName(	alt,
										inputModifiers)).thenReturn(Keyboard.L_ALT);
		when(keyboard.asKeySymbolName(	backspace,
										inputModifiers)).thenReturn(Keyboard.BACKSPACE);

		final KeyInputStringBuilderImpl keyInputStringBuilderImpl = new KeyInputStringBuilderImpl(keyboard);

		keyInputStringBuilderImpl.append("abc");
		keyInputStringBuilderImpl.append(altInput);
		keyInputStringBuilderImpl.append(backspaceInput);
		keyInputStringBuilderImpl.append(dInput);

		Assert.assertEquals("abd",
							keyInputStringBuilderImpl.toString());
	}
}
