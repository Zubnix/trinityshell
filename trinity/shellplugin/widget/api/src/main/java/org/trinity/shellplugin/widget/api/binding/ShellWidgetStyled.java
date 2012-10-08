package org.trinity.shellplugin.widget.api.binding;

import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shell.api.widget.BaseShellWidget;
import org.trinity.shell.api.widget.ShellWidgetView;

import com.google.common.eventbus.EventBus;

public class ShellWidgetStyled extends BaseShellWidget {

	@ViewAttribute(name = "objectName")
	private String name;
	@ViewReference
	private final ShellWidgetView view;

	public ShellWidgetStyled(	final EventBus eventBus,
								final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
								final PainterFactory painterFactory,
								final ShellWidgetView view) {
		super(	eventBus,
				shellDisplayEventDispatcher,
				painterFactory,
				view);
		this.view = view;
	}

	@ViewAttributeChanged("objectName")
	public void setName(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
