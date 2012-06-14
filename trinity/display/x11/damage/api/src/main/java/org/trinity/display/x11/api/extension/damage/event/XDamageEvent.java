package org.trinity.display.x11.api.extension.damage.event;

import org.trinity.display.x11.api.core.event.XEvent;
import org.trinity.display.x11.api.extension.damage.XDamage;
import org.trinity.foundation.shared.geometry.api.Rectangle;

// currently unused
// TODO documentation
/**
 * Currently unused.
 * 
 * @author Erik De Rijcke
 * @since 1.1
 */
public interface XDamageEvent extends XEvent {

	// level: DamageReportLevel
	// drawable: Drawable
	// damage: DAMAGE
	// more: Bool
	// timestamp: Timestamp
	// area: HierarchicalArea
	// drawable-geometry: HierarchicalArea

	XDamage getDamage();

	boolean isMore();

	Rectangle getArea();

	Rectangle getGeometry();
}
