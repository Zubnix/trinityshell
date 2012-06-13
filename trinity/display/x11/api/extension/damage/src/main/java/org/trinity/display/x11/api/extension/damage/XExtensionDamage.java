package org.trinity.display.x11.api.extension.damage;

import org.trinity.display.x11.api.core.XExtension;
import org.trinity.display.x11.api.core.XWindow;
import org.trinity.display.x11.api.extension.fixes.XFixesXServerRegion;

// currently unused
// TODO documentation
/**
 * Currently unused.
 * 
 * @author Erik De Rijcke
 * @since 1.1
 */
public interface XExtensionDamage extends XExtension {
	XDamage create(XWindow window, XDamageToReport damageReport);

	void subtract(	XDamage damage,
					XFixesXServerRegion repair,
					XFixesXServerRegion parts);
}
