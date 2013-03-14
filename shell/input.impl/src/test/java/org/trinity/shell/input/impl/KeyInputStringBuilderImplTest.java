package org.trinity.shell.input.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.trinity.foundation.api.display.server.input.Keyboard;
import org.trinity.foundation.api.display.shared.event.KeyNotify;
import org.trinity.foundation.api.display.shared.input.InputModifiers;
import org.trinity.foundation.api.display.shared.input.Key;
import org.trinity.foundation.api.display.shared.input.KeyboardInput;
import org.trinity.shell.input.impl.ShellKeyInputStringBuilderImpl;

public class KeyInputStringBuilderImplTest {

	@Test
	public void test() {
		final InputModifiers inputModifiers = mock(InputModifiers.class);

		final Key d = mock(Key.class);
		final KeyboardInput dKeyboardInput = mock(KeyboardInput.class);
		when(dKeyboardInput.getKey()).thenReturn(d);
		when(dKeyboardInput.getInputModifiers()).thenReturn(inputModifiers);
		final KeyNotify dInput = mock(KeyNotify.class);
		when(dInput.getInput()).thenReturn(dKeyboardInput);

		final Key alt = mock(Key.class);
		final KeyboardInput altKeyboardInput = mock(KeyboardInput.class);
		when(altKeyboardInput.getKey()).thenReturn(alt);
		when(altKeyboardInput.getInputModifiers()).thenReturn(inputModifiers);
		final KeyNotify altInput = mock(KeyNotify.class);
		when(altInput.getInput()).thenReturn(altKeyboardInput);

		final Key backspace = mock(Key.class);
		final KeyboardInput backspaceKeyboardInput = mock(KeyboardInput.class);
		when(backspaceKeyboardInput.getKey()).thenReturn(backspace);
		when(backspaceKeyboardInput.getInputModifiers()).thenReturn(inputModifiers);
		final KeyNotify backspaceInput = mock(KeyNotify.class);
		when(backspaceInput.getInput()).thenReturn(backspaceKeyboardInput);

		final Keyboard keyboard = mock(Keyboard.class);
		when(keyboard.asKeySymbolName(	d,
										inputModifiers)).thenReturn("d");
		when(keyboard.asKeySymbolName(	alt,
										inputModifiers)).thenReturn(Keyboard.L_ALT);
		when(keyboard.asKeySymbolName(	backspace,
										inputModifiers)).thenReturn(Keyboard.BACKSPACE);

		final ShellKeyInputStringBuilderImpl keyInputStringBuilderImpl = new ShellKeyInputStringBuilderImpl(keyboard);

		keyInputStringBuilderImpl.append("abc");
		keyInputStringBuilderImpl.append(altInput);
		keyInputStringBuilderImpl.append(backspaceInput);
		keyInputStringBuilderImpl.append(dInput);

		Assert.assertEquals("abd",
							keyInputStringBuilderImpl.toString());
	}
}
