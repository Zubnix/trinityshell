package org.trinity.shell.widget.api.view;

import java.util.concurrent.Future;

public interface ShellLabelView extends ShellWidgetView {
	Future<Void> update(String text);
}