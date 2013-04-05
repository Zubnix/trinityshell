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
package org.trinity.foundation.api.display.input;

import java.util.List;

import org.trinity.foundation.api.display.DisplayArea;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.KeyNotify;
import org.trinity.foundation.api.shared.OwnerThread;

import com.google.common.util.concurrent.ListenableFuture;

/***************************************
 * A text input device. It offers functionality to map to and from {@link Key}
 * input to key symbols, that is, the name or the character the key represents.
 * 
 *************************************** 
 */
@OwnerThread("Display")
public interface Keyboard extends InputDevice {

	/**
	 * 'Left shift' key name.
	 */
	String L_SHIFT = "L_SHIFT";
	/**
	 * 'Right shift' key name.
	 */
	String R_SHIFT = "R_SHIFT";
	/**
	 * 'Left control' key name.
	 */
	String L_CTRL = "L_CTRL";
	/**
	 * 'Right control' key name.
	 */
	String R_CTRL = "R_CTRL";
	/**
	 * 'Caps lock' key name.
	 */
	String CAPS_LOCK = "CAPS_LOCK";
	/**
	 * 'Shift lock' key name.
	 */
	String SHIFT_LOCK = "SHIFT_LOCK";
	/**
	 * 'Left meta' (mod4) key name.
	 */
	String L_META = "L_META";
	/**
	 * 'Right meta' (mod4) key name.
	 */
	String R_META = "R_META";
	/**
	 * 'Left alt' (mod1) key name.
	 */
	String L_ALT = "L_ALT";
	/**
	 * 'Right alt' (mod1) key name.
	 */
	String R_ALT = "R_ALT";
	/**
	 * 'Left super' key name.
	 */
	String L_SUPER = "L_SUPER";
	/**
	 * 'Right super' key name.
	 */
	String R_SUPER = "R_SUPER";
	/**
	 * 'Left hyper' key name.
	 */
	String L_HYPER = "L_HYPER";
	/**
	 * 'Right hyper' key name.
	 */
	String R_HYPER = "R_HYPER";
	/**
	 * 'Home' key name.
	 */
	String HOME = "HOME";
	/**
	 * 'Left arrow' key name.
	 */
	String LEFT = "LEFT";
	/**
	 * 'Up arrow' key name.
	 */
	String UP = "UP";
	/**
	 * 'Right arrow' key name.
	 */
	String RIGHT = "RIGHT";
	/**
	 * 'Down arrow' key name.
	 */
	String DOWN = "DOWN";
	/**
	 * 'Previous' key name.
	 */
	String PREV = "PREV";
	/**
	 * 'Page up' key name.
	 */
	String PG_UP = "PG_UP";
	/**
	 * 'Next' key name.
	 */
	String NEXT = "NEXT";
	/**
	 * 'Page down' key name.
	 */
	String PG_DOWN = "PG_DOWN";
	/**
	 * 'End' key name.
	 */
	String END = "END";
	/**
	 * 'Begin' key name.
	 */
	String BEGIN = "BEGIN";
	/**
	 * 'Escape' key name.
	 */
	String ESCAPE = "ESCAPE";
	/**
	 * 'Enter' key name.
	 */
	String ENTER = "ENTER";
	/**
	 * 'Backspace' key name.
	 */
	String BACKSPACE = "BACKSPACE";
	/**
	 * 'Tab' key name.
	 */
	String TAB = "TAB";
	/**
	 * 'Linefeed' key name.
	 */
	String LINEFEED = "LINEFEED";
	/**
	 * 'Clear' key name.
	 */
	String CLEAR = "CLEAR";
	/**
	 * 'Pause' key name.
	 */
	String PAUSE = "PAUSE";
	/**
	 * 'Scroll lock' key name.
	 */
	String SCRL_LOCK = "SCRL_LOCK";
	/**
	 * 'System request' key name.
	 */
	String SYS_REQ = "SYS_REQ";
	/**
	 * 'Delete' key name.
	 */
	String DELETE = "DELETE";
	/**
	 * 'Print' key name.
	 */
	String PRINT = "PRINT";
	/**
	 * 'Insert' key name.
	 */
	String INSERT = "INSERT";
	/**
	 * 'Number lock' key name.
	 */
	String NUM_LOCK = "NUM_LOCK";

	/***************************************
	 * Translate a <code>Key</code> with the given <code>InputModifiers</code>
	 * to it's textual representation. Usually this is one of the characters
	 * that is present on the physical key. For special cases where no clear
	 * physical character can be deduced, one of the statically defined key
	 * names in {@code Keyboard} is used.
	 * 
	 * @param key
	 *            a {@link Key}
	 * @param inputModifiers
	 *            {@link InputModifiers}
	 * @return The keysymbol name.
	 *************************************** 
	 */
	// TODO return listanablefuture?
	ListenableFuture<String> asKeySymbolName(	Key key,
												InputModifiers inputModifiers);

	/***************************************
	 * Translate the given keysymbol name to all possible {@link Key}s. A
	 * keysymbol name is usually one of the characters that is present on the
	 * physical key. For special cases check any of the statically defined key
	 * names in {@link Keyboard}.
	 * 
	 * @param keySymbolName
	 *            a keysymbol name.
	 * @return All possible {@link Key}s.
	 *************************************** 
	 */
	// TODO return listanablefuture?
	ListenableFuture<List<Key>> asKeys(String keySymbolName);

	/***************************************
	 * Create an <code>InputModifier</code> based on it's name. For a valid name
	 * check any of the statically defined names in {@link InputModifier}.
	 * 
	 * @param modifierName
	 *            The <code>InputModifier</code> name.
	 * @return an {@link InputModifier}
	 *************************************** 
	 */
	// TODO return listanablefuture?
	InputModifier modifier(String modifierName);

	/***************************************
	 * Create a group of <code>InputModifier</code>s based on the group members
	 * names. For a valid names check any of the statically defined names in
	 * {@link InputModifier}.
	 * 
	 * @param modifierNames
	 *            The {@link InputModifier} names.
	 * @return {@link InputModifiers}
	 *************************************** 
	 */
	// TODO return listanablefuture?
	InputModifiers modifiers(String... modifierNames);

	/***************************************
	 * Grab a {@link Key} of the bound {@link DisplayArea} or any of its
	 * children. Grabbing a {@link Key} will override the delivery of the
	 * corresponding {@link KeyNotify} event so it is only delivered to the
	 * grabber instead of delivering it anyone that is interested.
	 * <p>
	 * This method is usually used to install keybindings.
	 * 
	 * @param grabKey
	 *            The {@link Key} that should be grabbed
	 * @param withModifiers
	 *            The {@link InputModifiers} that should be active if a grab is
	 *            to take place.
	 *************************************** 
	 */
	// TODO grab key notify
	ListenableFuture<Void> grabKey(	DisplaySurface displaySurface,
									Key grabKey,
									InputModifiers withModifiers);

	/***************************************
	 * Release the grab on the specific {@link Key} with the specific
	 * {@link InputModifiers}.
	 * 
	 * @param ungrabKey
	 *            The {@link Key} that will be ungrabbed.
	 * @param withModifiers
	 *            the {@link InputModifiers}/
	 * @see #grabKey(Key, InputModifiers)
	 *************************************** 
	 */
	// TODO ungrab key notify
	ListenableFuture<Void> ungrabKey(	DisplaySurface displaySurface,
										Key ungrabKey,
										InputModifiers withModifiers);

	/***************************************
	 * Release the grab of the keyboard of the bound {@link DisplayArea}.
	 * 
	 * @see #grabKeyboard()
	 *************************************** 
	 */
	// TODO grab notify
	ListenableFuture<Void> ungrabKeyboard();

	/***************************************
	 * Grab the entire keyboard of the bound {@link DisplayArea} or any of its
	 * children. All {@link KeyNotify}s will be redirected.
	 * 
	 * @see #grabKey(Key, InputModifiers)
	 *************************************** 
	 */
	// TODO grab keyboard notify
	ListenableFuture<Void> grabKeyboard(DisplaySurface displaySurface);
}
