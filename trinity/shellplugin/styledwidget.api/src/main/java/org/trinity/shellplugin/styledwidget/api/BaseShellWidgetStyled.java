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
package org.trinity.shellplugin.styledwidget.api;

import org.trinity.foundation.api.render.PainterFactory;
import org.trinity.foundation.api.render.binding.refactor.model.View;
import org.trinity.foundation.api.render.binding.refactor.model.ViewProperty;
import org.trinity.foundation.api.render.binding.refactor.model.ViewPropertyChanged;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shell.api.widget.BaseShellWidget;

import com.google.common.eventbus.EventBus;

/***************************************
 * Base implementation of a {@link ShellWidgetStyled}. It has a single
 * {@link ViewProperty} with name "objectName" in order to be identified in a
 * style sheet. This {@code ViewProperty} can be changed and read through
 * {@link #setName(String)} and {@link #getName()} respectively.
 * 
 *************************************** 
 */
public class BaseShellWidgetStyled extends BaseShellWidget implements ShellWidgetStyled {

	private String name = getClass().getSimpleName();

	private final Object view;

	protected BaseShellWidgetStyled(final EventBus eventBus,
									final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
									final PainterFactory painterFactory,
									final Object view) {
		super(	eventBus,
				shellDisplayEventDispatcher,
				painterFactory);
		this.view = view;
	}

	@View
	public Object getView() {
		return this.view;
	}

	@ViewPropertyChanged("name")
	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@ViewProperty
	@Override
	public String getName() {
		return this.name;
	}
}
