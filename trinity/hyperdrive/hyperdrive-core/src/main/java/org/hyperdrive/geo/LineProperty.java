package org.hyperdrive.geo;

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
public class LineProperty {
	private int weight;

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
	public LineProperty(final int weight) {
		setWeight(weight);
	}

	/**
	 * 
	 * @return
	 */
	public int getWeight() {
		return this.weight;
	}
}