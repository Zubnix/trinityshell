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

import javax.annotation.concurrent.NotThreadSafe;

import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.KeyNotify;
import org.trinity.foundation.api.display.input.InputModifiers;
import org.trinity.foundation.api.display.input.Key;
import org.trinity.foundation.api.display.input.Keyboard;
import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.shell.api.input.ShellKeysBinding;
import org.trinity.shell.api.surface.ShellSurface;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.FutureCallback;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

import static com.google.common.util.concurrent.Futures.addCallback;

@NotThreadSafe
public class ShellKeysBindingImpl implements ShellKeysBinding {

	private final ShellSurface root;
	private final EventBus shellEventBus;
	private final List<Key> keys;
	private final InputModifiers inputModifiers;
	private final Runnable action;
	private final Keyboard keyboard;

	@Inject
	public ShellKeysBindingImpl(@Named("ShellRootSurface") final ShellSurface root,
								final Keyboard keyboard,
								@Named("ShellEventBus") final EventBus shellEventBus,
								@Assisted final List<Key> keys,
								@Assisted final InputModifiers inputModifiers,
								@Assisted final Runnable action) {
		this.root = root;
		this.keys = keys;
		this.inputModifiers = inputModifiers;
		this.action = action;
		this.shellEventBus = shellEventBus;
		this.keyboard = keyboard;
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

			// callback executed in same thread, should block (?)
			addCallback(this.root.getDisplaySurface(),
						new FutureCallback<DisplaySurface>() {
							@Override
							public void onSuccess(final DisplaySurface result) {
								ShellKeysBindingImpl.this.keyboard.grabKey(	result,
																			grabKey,
																			ShellKeysBindingImpl.this.inputModifiers);

							}

							@Override
							public void onFailure(final Throwable t) {
								// TODO Auto-generated method stub
								t.printStackTrace();
							}
						});
		}
	}

	@Subscribe
	public void handleKeyEvent(final KeyNotify keyNotify) {
		final KeyboardInput keyboardInput = keyNotify.getInput();
		if (this.keys.contains(keyboardInput.getKey()) && this.inputModifiers.equals(keyboardInput.getInputModifiers())) {
			this.action.run();
		}
	}

	@Override
	public void unbind() {
		for (final Key grabKey : this.keys) {

			// callback executed in same thread, should block (?)
			addCallback(this.root.getDisplaySurface(),
						new FutureCallback<DisplaySurface>() {
							@Override
							public void onSuccess(final DisplaySurface result) {
								ShellKeysBindingImpl.this.keyboard
										.ungrabKey(	result,
													grabKey,
													ShellKeysBindingImpl.this.inputModifiers);
							}

							@Override
							public void onFailure(final Throwable t) {
								// TODO Auto-generated method stub
								t.printStackTrace();
							}
						});
		}
		this.shellEventBus.unregister(this);
	}
}