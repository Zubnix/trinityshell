///*
// * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
// * This program is free software: you can redistribute it and/or modify it under
// * the terms of the GNU General Public License as published by the Free Software
// * Foundation, either version 3 of the License, or (at your option) any later
// * version. This program is distributed in the hope that it will be useful, but
// * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
// * details. You should have received a copy of the GNU General Public License
// * along with this program. If not, see <http://www.gnu.org/licenses/>.
// */
//package org.trinity.shell.input.impl;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import javax.annotation.concurrent.ThreadSafe;
//
//import org.trinity.foundation.api.display.DisplaySurface;
//import org.trinity.foundation.api.display.event.KeyNotify;
//import org.trinity.foundation.api.display.input.InputModifiers;
//import org.trinity.foundation.api.display.input.Key;
//import org.trinity.foundation.api.display.input.Keyboard;
//import org.trinity.foundation.api.display.input.KeyboardInput;
//import org.trinity.shell.api.input.KeyBindAction;
//import org.trinity.shell.api.input.ShellKeysBinding;
//import org.trinity.shell.api.surface.ShellSurfaceFactory;
//import org.trinity.shell.api.surface.ShellSurfaceParent;
//
//import com.google.common.eventbus.Subscribe;
//import com.google.common.util.concurrent.AsyncFunction;
//import com.google.common.util.concurrent.FutureCallback;
//import com.google.common.util.concurrent.ListenableFuture;
//import com.google.inject.Inject;
//import com.google.inject.assistedinject.Assisted;
//
//import static com.google.common.util.concurrent.Futures.addCallback;
//import static com.google.common.util.concurrent.Futures.transform;
//
//@ThreadSafe
//public class ShellKeysBindingImpl implements ShellKeysBinding {
//
//	private final ShellSurfaceFactory shellSurfaceFactory;
//	private final List<Key> keys;
//	private final InputModifiers inputModifiers;
//	private final KeyBindAction action;
//	private final Keyboard keyboard;
//
//	@Inject
//	public ShellKeysBindingImpl(final ShellSurfaceFactory shellSurfaceFactory,
//								final Keyboard keyboard,
//								@Assisted final List<Key> keys,
//								@Assisted final InputModifiers inputModifiers,
//								@Assisted final KeyBindAction action) {
//		this.shellSurfaceFactory = shellSurfaceFactory;
//		this.keys = new ArrayList<Key>(keys);
//		this.inputModifiers = inputModifiers;
//		this.action = action;
//		this.keyboard = keyboard;
//	}
//
//	@Override
//	public KeyBindAction getAction() {
//		return this.action;
//	}
//
//	@Override
//	public List<Key> getKeys() {
//		return Collections.unmodifiableList(this.keys);
//	}
//
//	@Override
//	public InputModifiers getInputModifiers() {
//		return this.inputModifiers;
//	}
//
//	@Override
//	public void bind() {
//		final ListenableFuture<ShellSurfaceParent> shellRootSurface = this.shellSurfaceFactory.getRootShellSurface();
//		final ListenableFuture<DisplaySurface> rootDisplaySurfaceFuture = transform(shellRootSurface,
//																					new AsyncFunction<ShellSurfaceParent, DisplaySurface>() {
//																						@Override
//																						public ListenableFuture<DisplaySurface> apply(final ShellSurfaceParent input)
//																								throws Exception {
//																							input.register(ShellKeysBindingImpl.this);
//																							return input
//																									.getDisplaySurface();
//																						}
//																					});
//
//		for (final Key grabKey : getKeys()) {
//			addCallback(rootDisplaySurfaceFuture,
//						new FutureCallback<DisplaySurface>() {
//							@Override
//							public void onSuccess(final DisplaySurface result) {
//								ShellKeysBindingImpl.this.keyboard.grabKey(	result,
//																			grabKey,
//																			ShellKeysBindingImpl.this.inputModifiers);
//
//							}
//
//							@Override
//							public void onFailure(final Throwable t) {
//								// TODO Auto-generated method stub
//								t.printStackTrace();
//							}
//						});
//		}
//	}
//
//	@Subscribe
//	public void handleKeyEvent(final KeyNotify keyNotify) {
//		final KeyboardInput keyboardInput = keyNotify.getInput();
//		if (this.keys.contains(keyboardInput.getKey()) && this.inputModifiers.equals(keyboardInput.getInputModifiers())) {
//			this.action.onKey(keyNotify);
//		}
//	}
//
//	@Override
//	public void unbind() {
//		final ListenableFuture<ShellSurfaceParent> shellRootSurface = this.shellSurfaceFactory.getRootShellSurface();
//		final ListenableFuture<DisplaySurface> rootDisplaySurfaceFuture = transform(shellRootSurface,
//																					new AsyncFunction<ShellSurfaceParent, DisplaySurface>() {
//																						@Override
//																						public ListenableFuture<DisplaySurface> apply(final ShellSurfaceParent input)
//																								throws Exception {
//																							input.unregister(ShellKeysBindingImpl.this);
//																							return input
//																									.getDisplaySurface();
//																						}
//																					});
//
//		for (final Key grabKey : getKeys()) {
//			addCallback(rootDisplaySurfaceFuture,
//						new FutureCallback<DisplaySurface>() {
//							@Override
//							public void onSuccess(final DisplaySurface result) {
//								ShellKeysBindingImpl.this.keyboard
//										.ungrabKey(	result,
//													grabKey,
//													ShellKeysBindingImpl.this.inputModifiers);
//							}
//
//							@Override
//							public void onFailure(final Throwable t) {
//								// TODO Auto-generated method stub
//								t.printStackTrace();
//							}
//						});
//		}
//	}
//}