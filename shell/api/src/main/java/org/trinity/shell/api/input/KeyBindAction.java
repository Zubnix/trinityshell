package org.trinity.shell.api.input;

import org.trinity.foundation.api.display.event.KeyNotify;

public interface KeyBindAction {
	/***************************************
	 * @param keyNotify
	 *************************************** 
	 */
	void onKey(KeyNotify keyNotify);
}
