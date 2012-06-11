package org.trinity.display.x11.api.extension.render;

import org.trinity.display.x11.api.extension.XExtension;
import org.trinity.display.x11.impl.XServerImpl;
import org.trinity.display.x11.impl.XWindowImpl;

//currently unused
//TODO documentation
/**
* Currently unused.
* 
* @author Erik De Rijcke
* @since 1.1
*/
public interface XExtensionXRender extends XExtension {
	// TODO needed for compositing

	XRenderPicture createPicture(XWindowImpl window);

	XRenderPictFormat findVisualFormat(final XServerImpl display,
	                                   long visualPeer);

	void XRenderComposite(XServerImpl display,
	                      XRenderPictOp pictOp,
	                      XRenderPicture src,
	                      XRenderPicture mask,
	                      XRenderPicture dst,
	                      int srcX,
	                      int srcY,
	                      int maskX,
	                      int maskY,
	                      int destinationX,
	                      int destinationY,
	                      int width,
	                      int height);
}
