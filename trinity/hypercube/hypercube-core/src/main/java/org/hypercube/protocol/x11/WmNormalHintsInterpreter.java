package org.hypercube.protocol.x11;

import org.fusion.x11.icccm.WmSizeHintsInstance;
import org.hypercube.protocol.GeometryPreferences;
import org.hypercube.protocol.ProtocolEvent;
import org.hyperdrive.core.ClientWindow;

final class WmNormalHintsInterpreter {

	private final XDesktopProtocol xDesktopProtocol;

	WmNormalHintsInterpreter(final XDesktopProtocol xDesktopProtocol) {
		this.xDesktopProtocol = xDesktopProtocol;
	}

	void handleWmNormalHints(final ClientWindow client,
			final WmSizeHintsInstance propertyInstance) {

		// query current geo preferences
		final ProtocolEvent<GeometryPreferences> geometryPreferences = this.xDesktopProtocol
				.query(client, ProtocolEvent.GEO_PREF);

		// read old preferences, if no previous ones were defined we fall back
		// to sane defaults
		final int oldX;
		final int oldY;
		final int oldWidth;
		final int oldHeight;
		final int oldMinWidth;
		final int oldMinHeight;
		final int oldMaxWidth;
		final int oldMaxHeight;
		final int oldWidthInc;
		final int oldHeightInc;
		final boolean oldVisibible;

		if (geometryPreferences == null) {
			oldX = client.getRelativeX();
			oldY = client.getRelativeY();
			oldWidth = client.getWidth();
			oldHeight = client.getHeight();
			oldMinWidth = client.getMinWidth();
			oldMinHeight = client.getMinHeight();
			oldMaxWidth = client.getMaxWidth();
			oldMaxHeight = client.getMaxHeight();
			oldWidthInc = client.getWidthIncrement();
			oldHeightInc = client.getHeightIncrement();
			oldVisibible = client.isVisible();
		} else {
			final GeometryPreferences geoPreferences = geometryPreferences
					.getEventArguments();
			oldX = geoPreferences.getX();
			oldY = geoPreferences.getY();
			oldWidth = geoPreferences.getWidth();
			oldHeight = geoPreferences.getHeight();
			oldMinWidth = geoPreferences.getMinWidth();
			oldMinHeight = geoPreferences.getMinHeight();
			oldMaxWidth = geoPreferences.getMaxWidth();
			oldMaxHeight = geoPreferences.getMaxHeight();
			oldWidthInc = geoPreferences.getWidthInc();
			oldHeightInc = geoPreferences.getHeightInc();
			oldVisibible = geoPreferences.isVisible();
		}

		// read new preferences
		final int x = propertyInstance.getX();
		final int y = propertyInstance.getY();
		final int width = propertyInstance.getWidth();
		final int height = propertyInstance.getHeight();
		final int minWidth = propertyInstance.getMinWidth();
		final int minHeight = propertyInstance.getMinHeight();
		final int maxWidth = propertyInstance.getMaxWidth();
		final int maxHeight = propertyInstance.getMaxHeight();
		final int widthInc = propertyInstance.getWidthInc();
		final int heightInc = propertyInstance.getHeightInc();

		// parse flags:
		final long normalHintFlags = propertyInstance.getFlags();

		// USPosition 1 User-specified x, y
		final boolean userSpecifiedPos = (normalHintFlags & 1) != 0;
		// USSize 2 User-specified width, height
		final boolean userSpecifiedSize = (normalHintFlags & 2) != 0;
		// PPosition 4 Program-specified position
		final boolean programPosition = (normalHintFlags & 4) != 0;
		// PSize 8 Program-specified size
		final boolean programSize = (normalHintFlags & 8) != 0;
		// PMinSize 16 Program-specified minimum size
		final boolean programMinSize = (normalHintFlags & 16) != 0;
		// PMaxSize 32 Program-specified maximum size
		final boolean programMaxSize = (normalHintFlags & 32) != 0;
		// PResizeInc 64 Program-specified resize increments
		final boolean programResizeIncrements = (normalHintFlags & 64) != 0;
		// PAspect 128 Program-specified min and max aspect ratios
		// final boolean programAspectRatios = (normalHintFlags & 128) != 0;
		// PBaseSize 256 Program-specified base size
		// final boolean programBaseSize = (normalHintFlags & 256) != 0;
		// PWinGravity 512 Program-specified window gravity
		// Window Managers MUST honor the win_gravity field of
		// WM_NORMAL_HINTS for both MapRequest and ConfigureRequest events
		// (ICCCM Version 2.0, §4.1.2.3 and §4.1.5)
		// final boolean programResizeGravity = (normalHintFlags & 512) != 0;

		final int newX;
		final int newY;
		final int newWidth;
		final int newHeight;
		final int newMinWidth;
		final int newMinHeight;
		final int newMaxWidth;
		final int newMaxHeight;
		final int newWidthInc;
		final int newHeightInc;
		final boolean newResizable;

		// interpret flags to determine which preferences need to be updated

		// FIXME icccm differentiate between user specified position and program
		// specified position. How is this usefull?
		if (userSpecifiedPos) {
			newX = x;
			newY = y;
		} else if (programPosition) {
			newX = x;
			newY = y;
		} else {
			newX = oldX;
			newY = oldY;
		}

		if (userSpecifiedSize) {
			newWidth = width;
			newHeight = height;
		} else if (programSize) {
			newWidth = width;
			newHeight = height;
		} else {
			newWidth = oldWidth;
			newHeight = oldHeight;
		}

		if (programMinSize) {
			newMinWidth = minWidth;
			newMinHeight = minHeight;
		} else {
			newMinWidth = oldMinWidth;
			newMinHeight = oldMinHeight;
		}

		if (programMaxSize) {
			newMaxWidth = maxWidth;
			newMaxHeight = maxHeight;
		} else {
			newMaxWidth = oldMaxWidth;
			newMaxHeight = oldMaxHeight;
		}

		if (programResizeIncrements) {
			newWidthInc = widthInc;
			newHeightInc = heightInc;
		} else {
			newWidthInc = oldWidthInc;
			newHeightInc = oldHeightInc;
		}

		if (programMinSize && programMaxSize && (minWidth == maxWidth)
				&& (minHeight == maxHeight)) {
			// From EWMH:
			// Windows can indicate that they are non-resizable by setting
			// minheight = maxheight and minwidth = maxwidth in the ICCCM
			// WM_NORMAL_HINTS property. The Window Manager MAY decorate such
			// windows differently.
			newResizable = false;
		} else {
			newResizable = true;
		}

		// TODO programAspectRatios, programBaseSize, programResizeGravity

		final GeometryPreferences newGeoPreferences = new GeometryPreferences(
				newX, newY, newWidth, newHeight, newMinWidth, newMinHeight,
				newMaxWidth, newMaxHeight, newWidthInc, newHeightInc,
				oldVisibible, newResizable);

		final ProtocolEvent<GeometryPreferences> protocolEvent = new ProtocolEvent<GeometryPreferences>(
				ProtocolEvent.GEO_PREF, newGeoPreferences);

		this.xDesktopProtocol.updateProtocolEvent(client, protocolEvent);
	}
}
