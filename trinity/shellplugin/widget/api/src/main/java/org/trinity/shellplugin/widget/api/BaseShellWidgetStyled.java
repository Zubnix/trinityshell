package org.trinity.shellplugin.widget.api;

import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shell.api.widget.BaseShellWidget;
import org.trinity.shell.api.widget.ShellWidgetView;
import org.trinity.shellplugin.widget.api.binding.ViewAttribute;
import org.trinity.shellplugin.widget.api.binding.ViewAttributeChanged;
import org.trinity.shellplugin.widget.api.binding.ViewReference;

import com.google.common.eventbus.EventBus;

public class BaseShellWidgetStyled extends BaseShellWidget implements ShellWidgetStyled {

	@ViewAttribute(name = "objectName")
	private String name = getClass().getSimpleName();
	@ViewReference
	private final ShellWidgetView view;

	public BaseShellWidgetStyled(	final EventBus eventBus,
									final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
									final PainterFactory painterFactory,
									final ShellWidgetView view) {
		super(	eventBus,
				shellDisplayEventDispatcher,
				painterFactory,
				view);
		this.view = view;
	}

	@Override
	@ViewAttributeChanged("objectName")
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}
}
