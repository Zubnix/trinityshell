package org.trinity.display.x11.api.extension.damage;

import org.trinity.display.x11.api.extension.XExtension;
import org.trinity.display.x11.api.extension.fixes.XFixesXServerRegion;
import org.trinity.display.x11.impl.XWindowImpl;

//currently unused
//TODO documentation
/**
 * Currently unused.
 * 
 * @author Erik De Rijcke
 * @since 1.1
 */
public interface XExtensionDamage extends XExtension {
	XDamage create(XWindowImpl window, XDamageToReport damageReport);

	void subtract(XDamage damage, XFixesXServerRegion repair,
			XFixesXServerRegion parts);
}
