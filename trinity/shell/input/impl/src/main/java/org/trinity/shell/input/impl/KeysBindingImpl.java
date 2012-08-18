package org.trinity.shell.input.impl;

import java.util.List;

import org.trinity.foundation.display.api.event.KeyNotifyEvent;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Key;
import org.trinity.foundation.input.api.KeyboardInput;
import org.trinity.shell.core.api.ShellRenderArea;
import org.trinity.shell.input.api.KeysBinding;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

public class KeysBindingImpl implements KeysBinding {

	private final ShellRenderArea root;
	private final EventBus shellEventBus;
	private final List<Key> keys;
	private final InputModifiers inputModifiers;
	private final Runnable action;

	@Inject
	public KeysBindingImpl(	@Named("shellRootRenderArea") final ShellRenderArea root,
							@Named("shellEventBus") final EventBus shellEventBus,
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
			this.root.getDisplayRenderArea().grabKey(	grabKey,
														this.inputModifiers);
		}
	}

	@Subscribe
	public void handleKeyEvent(final KeyNotifyEvent keyNotifyEvent) {
		final KeyboardInput keyboardInput = keyNotifyEvent.getInput();
		if (this.keys.contains(keyboardInput.getKey())
				&& this.inputModifiers.equals(keyboardInput.getModifiers())) {
			this.action.run();
		}
	}

	@Override
	public void unbind() {
		for (final Key grabKey : this.keys) {
			this.root.getDisplayRenderArea().ungrabKey(	grabKey,
														this.inputModifiers);
		}
		this.shellEventBus.unregister(this);
	}
}