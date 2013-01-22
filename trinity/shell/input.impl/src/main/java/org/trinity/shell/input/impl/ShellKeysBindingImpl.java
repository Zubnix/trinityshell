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
package org.trinity.shell.input.impl;

import java.util.List;

import org.trinity.foundation.api.display.event.KeyNotifyEvent;
import org.trinity.foundation.api.display.input.InputModifiers;
import org.trinity.foundation.api.display.input.Key;
import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.shell.api.input.ShellKeysBinding;
import org.trinity.shell.api.surface.ShellSurface;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

public class ShellKeysBindingImpl implements ShellKeysBinding {

	private final ShellSurface root;
	private final EventBus shellEventBus;
	private final List<Key> keys;
	private final InputModifiers inputModifiers;
	private final Runnable action;

	@Inject
	public ShellKeysBindingImpl(@Named("ShellRootSurface") final ShellSurface root,
								@Named("ShellEventBus") final EventBus shellEventBus,
								@Assisted final List<Key> keys,
								@Assisted final InputModifiers inputModifiers,
								@Assisted final Runnable action) {
		this.root = root;
		this.keys = keys;
		this.inputModifiers = inputModifiers;
		this.action = action;
		this.shellEventBus = shellEventBus;
	}

	@Override
	public Runnable getAction() {
		return this.action;
	}

	@Override
	public List<Key> getKeys() {
		return this.keys;
	}

	@Override
	public InputModifiers getInputModifiers() {
		return this.inputModifiers;
	}

	@Override
	public void bind() {
		this.shellEventBus.register(this);
		for (final Key grabKey : this.keys) {
			this.root.getDisplaySurface().get().grabKey(grabKey,
														this.inputModifiers);
		}
	}

	@Subscribe
	public void handleKeyEvent(final KeyNotifyEvent keyNotifyEvent) {
		final KeyboardInput keyboardInput = keyNotifyEvent.getInput();
		if (this.keys.contains(keyboardInput.getKey()) && this.inputModifiers.equals(keyboardInput.getInputModifiers())) {
			this.action.run();
		}
	}

	@Override
	public void unbind() {
		for (final Key grabKey : this.keys) {
			this.root.getDisplaySurface().get().ungrabKey(	grabKey,
															this.inputModifiers);
		}
		this.shellEventBus.unregister(this);
	}
}