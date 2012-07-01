package org.trinity.display.x11.impl.xcb;

import org.trinity.display.x11.core.api.XColormap;
import org.trinity.display.x11.core.api.XVisual;
import org.trinity.display.x11.core.api.XWindowAttributes;

public class XWindowAttributesImpl implements XWindowAttributes {

	private final boolean backingStore;
	private final int sequence;
	private final XVisual visual;
	private final int bitGravity;
	private final int winGravity;
	private final int backingPlanes;
	private final int backingPixel;
	private final boolean saveUnder;
	private final boolean mapInstalled;
	private final int mapState;
	private final boolean overrideRedirect;
	private final XColormap colormap;
	private final int allEventMasks;
	private final int yourEventMask;
	private final int doNotPropagateMask;

	public XWindowAttributesImpl(	final boolean backingStore,
									final int sequence,
									final XVisual visual,
									final int bitGravity,
									final int winGravity,
									final int backingPlanes,
									final int backingPixel,
									final boolean saveUnder,
									final boolean mapInstalled,
									final int mapState,
									final boolean overrideRedirect,
									final XColormap colormap,
									final int allEventMasks,
									final int yourEventMask,
									final int doNotPropagateMask) {
		this.backingStore = backingStore;
		this.sequence = sequence;
		this.visual = visual;
		this.bitGravity = bitGravity;
		this.winGravity = winGravity;
		this.backingPlanes = backingPlanes;
		this.backingPixel = backingPixel;
		this.saveUnder = saveUnder;
		this.mapInstalled = mapInstalled;
		this.mapState = mapState;
		this.overrideRedirect = overrideRedirect;
		this.colormap = colormap;
		this.allEventMasks = allEventMasks;
		this.yourEventMask = yourEventMask;
		this.doNotPropagateMask = doNotPropagateMask;
	}

	@Override
	public boolean hasBackingStore() {
		return this.backingStore;
	}

	@Override
	public int sequence() {
		return this.sequence;
	}

	@Override
	public XVisual getVisual() {
		return this.visual;
	}

	@Override
	public int getBitGravity() {
		return this.bitGravity;
	}

	@Override
	public int getWinGravity() {
		return this.winGravity;
	}

	@Override
	public int getBackingPlanes() {
		return this.backingPlanes;
	}

	@Override
	public int getBackingPixel() {
		return this.backingPixel;
	}

	@Override
	public boolean isSaveUnder() {
		return this.saveUnder;
	}

	@Override
	public boolean isMapInstalled() {
		return this.mapInstalled;
	}

	@Override
	public int getMapState() {
		return this.mapState;
	}

	@Override
	public boolean isOverrideRedirect() {
		return this.overrideRedirect;
	}

	@Override
	public XColormap getColormap() {
		return this.colormap;
	}

	@Override
	public int getAllEventMasks() {
		return this.allEventMasks;
	}

	@Override
	public int getYourEventMask() {
		return this.yourEventMask;
	}

	@Override
	public int getDoNotPropagateMask() {
		return this.doNotPropagateMask;
	}

}
