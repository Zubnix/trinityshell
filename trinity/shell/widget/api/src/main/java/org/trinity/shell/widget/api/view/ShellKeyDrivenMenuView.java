package org.trinity.shell.widget.api.view;

import java.util.List;
import java.util.concurrent.Future;

public interface ShellKeyDrivenMenuView extends ShellWidgetView {
	Future<Void> clear();

	Future<Void> activate();

	Future<Void> deactivate();

	Future<Void> update(String input,
						List<String> filteredChoices,
						int activeChoiceIdx);
}