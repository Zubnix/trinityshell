package org.trinity.display.x11.api.extension.shape;

import org.trinity.display.x11.api.core.XExtension;
import org.trinity.display.x11.api.core.XWindow;

// currently unused
// TODO documentation
/**
 * Currently unused.
 * 
 * @author Erik De Rijcke
 * @since 1.1
 */
public interface XExtensionXShape extends XExtension {
	// TODO needed for compositing
	void selectInput(XWindow window, long shapeNotifyMask);
}