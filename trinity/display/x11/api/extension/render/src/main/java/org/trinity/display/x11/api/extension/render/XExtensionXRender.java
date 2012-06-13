package org.trinity.display.x11.api.extension.render;

import org.trinity.display.x11.api.core.XDisplayServer;
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
public interface XExtensionXRender extends XExtension {
	// TODO needed for compositing

	XRenderPicture createPicture(XWindow window);

	XRenderPictFormat findVisualFormat(	final XDisplayServer display,
										long visualPeer);

	void XRenderComposite(	final XDisplayServer display,
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
