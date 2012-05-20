package org.hydrogen.display.api.input;

/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public enum InputModifierName {
	// TODO merge with specialkeyname?

	/**
	 * shift key
	 */
	MOD_SHIFT, MOD_LOCK, MOD_CTRL,
	/**
	 * Alt
	 */
	MOD_1,
	/**
	 * numlock
	 */
	MOD_2, MOD_3,
	/**
	 * Meta key (windows, cmd, ..)
	 */
	MOD_4, MOD_5, MOD_ANY;
}