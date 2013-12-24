package org.trinity.foundation.api.shared;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SizeTest {

	@Test
	public void testEquals(){
		//given
		//2 size object with the same width and height
		final Size size0 = new Size(10,20);
		final Size size1 = new Size(10,20);

		//when
		//the size objects are compared for equality
		final boolean equals0 = size0.equals(size1);
		final boolean equals1 = size1.equals(size0);

		//then
		//both size objects are considered equal
		assertTrue(equals0);
		assertTrue(equals1);
	}

	@Test
	public void testHashCode(){
		//given
		//2 size object with the same width and height
		final Size size0 = new Size(10,20);
		final Size size1 = new Size(10,20);

		//when
		//both size objects hashcodes are compared
		final int hashCode0 = size0.hashCode();
		final int hashCode1 = size1.hashCode();

		//then
		//both hashcodes are the same
		assertEquals(hashCode0,hashCode1);
	}
}
