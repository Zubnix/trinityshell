package org.trinity.foundation.api.shared;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CoordinateTest {

	@Test
	public void testEquals() {
		//given
		//2 coordinates
		//both coordinates have the same x and y value
		final Coordinate coordinate0 = new Coordinate(1,
													  2);
		final Coordinate coordinate1 = new Coordinate(1,
													  2);

		//when
		//both coordinates are compared for equality
		final boolean equals0 = coordinate0.equals(coordinate1);
		final boolean equals1 = coordinate1.equals(coordinate0);

		//then
		//both coordinates are considered equal
		assertTrue(equals0);
		assertTrue(equals1);
	}

	@Test
	public void testHashCode() {
		//given
		//2 coordinates
		//both coordinates have the same x and y value
		final Coordinate coordinate0 = new Coordinate(1,
													  2);
		final Coordinate coordinate1 = new Coordinate(1,
													  2);

		//when
		//both coordinates' hashcodes are compared
		final int hashCode0 = coordinate0.hashCode();
		final int hashCode1 = coordinate1.hashCode();

		//then
		//the hashcodes are the same
		assertEquals(hashCode0,
					 hashCode1);
	}
}
