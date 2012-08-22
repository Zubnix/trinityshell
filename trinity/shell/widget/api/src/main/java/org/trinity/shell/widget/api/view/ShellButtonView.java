package org.trinity.shell.widget.api.view;

import java.util.concurrent.Future;

public interface ShellButtonView extends ShellWidgetView {
	Future<Void> pressed();

	Future<Void> released();
}