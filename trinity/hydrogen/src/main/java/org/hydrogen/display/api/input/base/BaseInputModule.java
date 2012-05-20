package org.hydrogen.display.api.input.base;

import org.hydrogen.display.api.input.Button;
import org.hydrogen.display.api.input.Input;
import org.hydrogen.display.api.input.InputModifiers;
import org.hydrogen.display.api.input.KeyboardInput;
import org.hydrogen.display.api.input.MouseInput;

import com.google.inject.AbstractModule;

public class BaseInputModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Button.class).to(BaseButton.class);
		bind(Input.class).to(BaseInput.class);
		bind(InputModifiers.class).to(BaseInputModifiers.class);
		bind(KeyboardInput.class).to(BaseKeyboardInput.class);
		bind(MouseInput.class).to(BaseMouseInput.class);
	}

}
