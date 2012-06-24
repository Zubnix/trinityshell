package org.trinity.display.x11.api.core;

public interface XWindowAttributes {
	boolean hasBackingStore();

	int sequence();

	XVisual getVisual();

	int getBitGravity();

	int getWinGravity();

	int getBackingPlanes();

	int getBackingPixel();

	boolean isSaveUnder();

	boolean isMapInstalled();

	int getMapState();

	boolean isOverrideRedirect();

	XColormap getColormap();

	int getAllEventMasks();

	int getYourEventMask();

	int getDoNotPropagateMask();
}
