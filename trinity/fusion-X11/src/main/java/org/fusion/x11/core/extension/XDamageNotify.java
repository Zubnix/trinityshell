package org.fusion.x11.core.extension;

import org.fusion.x11.core.XDrawable;
import org.fusion.x11.core.XRectangle;
import org.hydrogen.display.api.event.DisplayEvent;
import org.hydrogen.display.api.event.DisplayEventType;

//currently unused
//TODO documentation
/**
* Currently unused.
* 
* @author Erik De Rijcke
* @since 1.1
*/
public interface XDamageNotify extends DisplayEvent {

	public static final DisplayEventType TYPE = new DisplayEventType();

	// level: DamageReportLevel
	// drawable: Drawable
	// damage: DAMAGE
	// more: Bool
	// timestamp: Timestamp
	// area: HierarchicalArea
	// drawable-geometry: HierarchicalArea

	XDamage getDamage();

	boolean isMore();

	XRectangle getArea();

	XRectangle getGeometry();

	@Override
	public XDrawable getEventSource();
}
