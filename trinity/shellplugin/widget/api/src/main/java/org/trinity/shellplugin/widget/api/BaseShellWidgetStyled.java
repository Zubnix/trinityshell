/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
