package org.hyperdrive.api.widget;

import java.util.List;

import org.hydrogen.api.display.input.KeyboardInput;

public interface KeyDrivenMenu extends Widget {

	public interface View extends Widget.View {
		PaintInstruction<Void> clear();

		PaintInstruction<Void> update(String input,
				List<String> possibleValues, int activeValue);

		PaintInstruction<Void> startedKeyListening();

		PaintInstruction<Void> stoppedKeyListening();
	}

	@Override
	public View getView();

	List<String> getAllChoices();

	String getFilter();

	List<String> getFilteredChoices();

	int getChoosenFilteredChoiceIdx();

	@Override
	void onKeyboardPressed(final KeyboardInput input);

	@Override
	void onKeyboardReleased(final KeyboardInput input);
}
