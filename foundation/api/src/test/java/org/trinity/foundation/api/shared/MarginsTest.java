package org.trinity.foundation.api.shared;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MarginsTest {

	@Test
	public void testEquals() {
		//given
		//2 margins
		//both coordinates have the same top, right, bottom and left margin value
		final Margins margins0 = new Margins(1,
											 2,
											 3,
											 4);
		final Margins margins1 = new Margins(1,
											 2,
											 3,
											 4);

		//when
		//both margins are compared for equality
		final boolean equals0 = margins0.equals(margins1);
		final boolean equals1 = margins1.equals(margins0);

		//then
		//both margins are considered equal
		assertTrue(equals0);
		assertTrue(equals1);
	}

	@Test
	public void testHashCode() {
		//given
		//2 margins
		//both coordinates have the same top, right, bottom and left margin value
		final Margins margins0 = new Margins(1,
											 2,
											 3,
											 4);
		final Margins margins1 = new Margins(1,
											 2,
											 3,
											 4);

		//when
		//both margins' hashcodes are compared
		final int hashCode0 = margins0.hashCode();
		final int hashCode1 = margins1.hashCode();

		//then
		//the hashcodes are the same
		assertEquals(hashCode0,
					 hashCode1);
	}
}
