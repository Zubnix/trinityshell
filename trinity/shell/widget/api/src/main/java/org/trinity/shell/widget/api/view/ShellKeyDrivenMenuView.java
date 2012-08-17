package org.trinity.shell.widget.api.view;

import java.util.List;

public interface ShellKeyDrivenMenuView extends ShellWidgetView {
	void clear();

	void activate();

	void deactivate();

	void update(String input,
				List<String> filteredChoices,
				int activeChoiceIdx);
}