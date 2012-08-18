/*
 * This file is part of HyperDrive. HyperDrive is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. HyperDrive is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with HyperDrive. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.geo.api;

/**
 * A <code>ShellGeoTransformation</code> represents a geometric transformation
 * in time. Current geometric property names end in 0, new ones in 1. A Delta
 * value is the subtraction of the new value with the current value so that:<br/>
 * <code>geoTransformation.getDeltaX() == geoTransformation.getX1() - geoTransformation.getX0()</code>
 * <br/>
 * returns true.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ShellGeoTransformation {
	private final int x0;
	private final int y0;
	private final int width0;
	private final int height0;

	private final int x1;
	private final int y1;
	private final int width1;
	private final int height1;

	private final boolean visible0;
	private final boolean visible1;

	private final int deltaX;
	private final int deltaY;

	private final int deltaWidth;
	private final int deltaHeight;

	private final ShellGeoNode parent0;
	private final ShellGeoNode parent1;

	/**
	 * Create a new <code>ShellGeoTransformation</code> with a number of current
	 * and new geometric values.
	 * 
	 * @param x0
	 *            The current relative X coordinate in pixels.
	 * @param y0
	 *            The current relative Y coordinate in pixels.
	 * @param width0
	 *            The current horizontal size in pixels.
	 * @param height0
	 *            The current vertical size in pixels.
	 * @param x1
	 *            The new relative X coordinate in pixels.
	 * @param y1
	 *            The new relative Y coordinate in pixels.
	 * @param width1
	 *            The new horizontal size in pixels.
	 * @param height1
	 *            The new vertical size in pixels.
	 * @param visible0
	 *            The current visibility.
	 * @param visible1
	 *            The new visibility.
	 */
	public ShellGeoTransformation(	final int x0,
									final int y0,
									final int width0,
									final int height0,
									final boolean visible0,
									final ShellGeoNode parent0,
									final int x1,
									final int y1,
									final int width1,
									final int height1,
									final boolean visible1,
									final ShellGeoNode parent1) {
		this.x0 = x0;
		this.y0 = y0;
		this.width0 = width0;
		this.height0 = height0;

		this.x1 = x1;
		this.y1 = y1;
		this.width1 = width1;
		this.height1 = height1;

		this.visible0 = visible0;
		this.visible1 = visible1;

		this.deltaX = this.x1 - this.x0;
		this.deltaY = this.y1 - this.y0;

		this.deltaWidth = this.width1 - this.width0;
		this.deltaHeight = this.height1 - this.height0;

		this.parent0 = parent0;
		this.parent1 = parent1;
	}

	/**
	 * The absolute difference between the current vertical height and the new
	 * vertical height.
	 * <p>
	 * This delta value is equal to:
	 * <p>
	 * <code>this.height1 - this.height0</code>
	 * 
	 * @return The delta height value.
	 */
	public int getDeltaHeight() {
		return this.deltaHeight;
	}

	/**
	 * The absolute difference between the current horizontal height and the new
	 * horizontal height.
	 * <p>
	 * This delta value is equal to:
	 * <p>
	 * <code>this.width1 - this.width0</code>
	 * 
	 * @return The delta width value.
	 */
	public int getDeltaWidth() {
		return this.deltaWidth;
	}

	/**
	 * The absolute difference between the current relative X coordinate and the
	 * new relative X coordinate
	 * <p>
	 * This delta X value is equal to:
	 * <p>
	 * <code>this.x1 - this.x0</code>
	 * 
	 * @return The delta X value.
	 */
	public int getDeltaX() {
		return this.deltaX;
	}

	/**
	 * The absolute difference between the current relative Y coordinate and the
	 * new relative Y coordinate
	 * <p>
	 * This delta Y value is equal to:
	 * <p>
	 * <code>this.y1 - this.y0</code>
	 * 
	 * @return The delta Y value.
	 */
	public int getDeltaY() {
		return this.deltaY;
	}

	/**
	 * The current vertical size in pixels.
	 * 
	 * @return The current height in pixels.
	 */
	public int getHeight0() {
		return this.height0;
	}

	/**
	 * The new vertical size in pixels.
	 * 
	 * @return The new height in pixels.
	 */
	public int getHeight1() {
		return this.height1;
	}

	/**
	 * The current horizontal size in pixels.
	 * 
	 * @return The current width in pixels.
	 */
	public int getWidth0() {
		return this.width0;
	}

	/**
	 * The new horizontal size in pixels.
	 * 
	 * @return The new width in pixels.
	 */
	public int getWidth1() {
		return this.width1;
	}

	/**
	 * The current relative X coordinate value.
	 * 
	 * @return The current relative X coordinate in pixels.
	 */
	public int getX0() {
		return this.x0;
	}

	/**
	 * The new relative X coordinate value.
	 * 
	 * @return The new relative X coordinate in pixels.
	 */
	public int getX1() {
		return this.x1;
	}

	/**
	 * The current relative Y coordinate value.
	 * 
	 * @return The current relative Y coordinate in pixels.
	 */
	public int getY0() {
		return this.y0;
	}

	/**
	 * The new relative Y coordinate value.
	 * 
	 * @return The new relative Y coordinate in pixels.
	 */
	public int getY1() {
		return this.y1;
	}

	/**
	 * The current visibility.
	 * 
	 * @return True if currently visible, false if not.
	 */
	public boolean isVisible0() {
		return this.visible0;
	}

	/**
	 * The new visibility.
	 * 
	 * @return True if should be visible, false if not.
	 */
	public boolean isVisible1() {
		return this.visible1;
	}

	// @Override
	// public String toString() {
	// return String
	// .format("( x1:%d, y1:%d + width1:%d x height1:%d,\n visible1: %b, parent1: %s )\n -"
	// +
	// "( x0:%d, y0:%d + width0:%d x height0:%d,\n visible0: %b, parent0: %s )\n = "
	// + "( deltaX:%d , deltaY:%d + deltaWidth:%d x deltaHeight:%d )",
	// getX1(), getY1(), getWidth1(), getHeight1(),
	// isVisible1(), getParent1(), getX0(), getY0(),
	// getWidth0(), getHeight0(), isVisible0(), getParent0(),
	// getDeltaX(), getDeltaY(), getDeltaWidth(),
	// getDeltaHeight());
	// }

	/**
	 * @return
	 */
	public ShellGeoNode getParent0() {
		return this.parent0;
	}

	/**
	 * @return
	 */
	public ShellGeoNode getParent1() {
		return this.parent1;
	}
}
