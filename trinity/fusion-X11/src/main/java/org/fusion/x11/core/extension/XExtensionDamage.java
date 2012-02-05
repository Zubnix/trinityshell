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
public interface XExtensionDamage extends XExtension {
	XDamage create(XWindow window, XDamageToReport damageReport);

	void subtract(XDamage damage, XFixesXServerRegion repair,
			XFixesXServerRegion parts);
}
