package org.fusion.x11.core.extension;

import org.fusion.x11.core.XWindow;

//currently unused
//TODO documentation
/**
* Currently unused.
* 
* @author Erik De Rijcke
* @since 1.1
*/
public interface XExtensionXShape extends XExtension {
	// TODO needed for compositing
	void selectInput(XWindow window,
	                 long shapeNotifyMask);
}