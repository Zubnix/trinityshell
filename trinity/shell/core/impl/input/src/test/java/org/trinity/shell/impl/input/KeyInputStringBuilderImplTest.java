package org.trinity.shell.impl.input;

import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.trinity.foundation.input.api.Keyboard;

public class KeyInputStringBuilderImplTest {

	@Test
	public void test() {
		final Keyboard keyboard = mock(Keyboard.class);
		final KeyInputStringBuilderImpl keyInputStringBuilderImpl = new KeyInputStringBuilderImpl(keyboard);
	}
}
