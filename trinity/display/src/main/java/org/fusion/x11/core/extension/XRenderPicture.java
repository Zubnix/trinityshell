package org.fusion.x11.core.extension;

import org.fusion.x11.core.XID;
import org.trinity.core.display.api.DisplayResource;

//currently unused
//TODO documentation
/**
* Currently unused.
* 
* @author Erik De Rijcke
* @since 1.1
*/
public interface XRenderPicture extends DisplayResource {
	@Override
	public XID getDisplayResourceHandle();
}
