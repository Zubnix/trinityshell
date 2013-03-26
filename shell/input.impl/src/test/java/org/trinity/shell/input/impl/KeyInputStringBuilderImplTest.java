package org.trinity.shell.input.impl;

import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;
import org.trinity.foundation.api.display.event.KeyNotify;
import org.trinity.foundation.api.display.input.InputModifiers;
import org.trinity.foundation.api.display.input.Key;
import org.trinity.foundation.api.display.input.Keyboard;
import org.trinity.foundation.api.display.input.KeyboardInput;

import com.google.common.util.concurrent.ListenableFuture;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KeyInputStringBuilderImplTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test() throws InterruptedException, ExecutionException {
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
		final ListenableFuture<String> dFuture = mock(ListenableFuture.class);
		when(dFuture.get()).thenReturn("d");
		when(keyboard.asKeySymbolName(	d,
										inputModifiers)).thenReturn(dFuture);
		final ListenableFuture<String> laltFuture = mock(ListenableFuture.class);
		when(laltFuture.get()).thenReturn(Keyboard.L_ALT);
		when(keyboard.asKeySymbolName(	alt,
										inputModifiers)).thenReturn(laltFuture);
		final ListenableFuture<String> backspaceFuture = mock(ListenableFuture.class);
		when(backspaceFuture.get()).thenReturn(Keyboard.BACKSPACE);
		when(keyboard.asKeySymbolName(	backspace,
										inputModifiers)).thenReturn(backspaceFuture);

		final ShellKeyInputStringBuilderImpl keyInputStringBuilderImpl = new ShellKeyInputStringBuilderImpl(keyboard);

		keyInputStringBuilderImpl.append("abc");
		keyInputStringBuilderImpl.append(altInput);
		keyInputStringBuilderImpl.append(backspaceInput);
		keyInputStringBuilderImpl.append(dInput);

		Assert.assertEquals("abd",
							keyInputStringBuilderImpl.getString());
	}
}
