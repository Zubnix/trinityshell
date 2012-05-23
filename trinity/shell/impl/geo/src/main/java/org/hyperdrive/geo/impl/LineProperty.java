/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hyperdrive.geo.impl;

import org.hyperdrive.geo.api.LayoutProperty;
import org.hyperdrive.geo.api.base.Margins;

/**
 * Determines the weight of the child when calculating it's width or height,
 * depending if the <code>GeoManagerLine</code> layouts horizontal or vertical.
 * <p>
 * A child's weight is compared to other childeren's weight. This relative
 * weight is then used to determine how much a child's height or width needs to
 * be adjusted. For example: A child with weight 2 will be resized with a delta
 * value of twice that of a child with a weight of 1 because 2 / 1 = 2.
 * <p>
 * When a weight of 0 or less is given, the child's dimension will be static.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class LineProperty implements LayoutProperty {
	private int weight;
	private Margins margins;

	/**
	 * A weight of 0 or lower indicates a static size.
	 * 
	 * @param weight
	 */
	public void setWeight(int weight) {
		if (weight < 0) {
			weight = 0;
		}
		this.weight = weight;
	}

	/**
	 * 
	 * @param weight
	 */
	public LineProperty(final int weight, final Margins margins) {
		setWeight(weight);
		setMargins(margins);
	}

	/**
	 * 
	 * @return
	 */
	public int getWeight() {
		return this.weight;
	}

	public Margins getMargins() {
		return this.margins;
	}

	public void setMargins(final Margins margins) {
		this.margins = margins;
	}
}