package org.fusion.x11.core.extension;

import org.fusion.x11.core.XDisplay;
import org.fusion.x11.core.XWindow;

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

	XRenderPicture createPicture(XWindow window);

	XRenderPictFormat findVisualFormat(final XDisplay display,
	                                   long visualPeer);

	void XRenderComposite(XDisplay display,
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
