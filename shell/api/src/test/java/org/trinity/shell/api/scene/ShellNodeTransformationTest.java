package org.trinity.shell.api.scene;

import com.google.common.base.Optional;
import org.junit.Test;
import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.foundation.api.shared.Size;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShellNodeTransformationTest {
	@Test
	public void testEquals() {
		//given 2 shell node transformation objects with the same transformation
		final Coordinate cor0 = mock(Coordinate.class);
		final Coordinate cor1 = mock(Coordinate.class);

		final Size size0 = mock(Size.class);
		final Size size1 = mock(Size.class);

		final Rectangle rect0 = mock(Rectangle.class);
		final Rectangle rect1 = mock(Rectangle.class);

		when(rect0.getPosition()).thenReturn(cor0);
		when(rect0.getSize()).thenReturn(size0);

		when(rect1.getPosition()).thenReturn(cor1);
		when(rect1.getSize()).thenReturn(size1);

		final ShellNodeParent parent0 = mock(ShellNodeParent.class);
		final ShellNodeParent parent1 = mock(ShellNodeParent.class);

		final ShellNodeTransformation shellNodeTransformation0 = new ShellNodeTransformation(rect0,
																							 Optional.of(parent0),
																							 rect1,
																							 Optional.of(parent1));
		final ShellNodeTransformation shellNodeTransformation1 = new ShellNodeTransformation(rect0,
																							 Optional.of(parent0),
																							 rect1,
																							 Optional.of(parent1));

		//when
		//the 2 objects are compare for equality
		final boolean equal0 = shellNodeTransformation0.equals(shellNodeTransformation1);
		final boolean equal1 = shellNodeTransformation1.equals(shellNodeTransformation0);

		//then
		//both objects are considered equal
		assertTrue(equal0);
		assertTrue(equal1);
	}

	@Test
	public void testHashCode() {
		//given 2 shell node transformation objects with the same transformation
		final Coordinate cor0 = mock(Coordinate.class);
		final Coordinate cor1 = mock(Coordinate.class);

		final Size size0 = mock(Size.class);
		final Size size1 = mock(Size.class);

		final Rectangle rect0 = mock(Rectangle.class);
		final Rectangle rect1 = mock(Rectangle.class);

		when(rect0.getPosition()).thenReturn(cor0);
		when(rect0.getSize()).thenReturn(size0);

		when(rect1.getPosition()).thenReturn(cor1);
		when(rect1.getSize()).thenReturn(size1);

		final ShellNodeParent parent0 = mock(ShellNodeParent.class);
		final ShellNodeParent parent1 = mock(ShellNodeParent.class);

		final ShellNodeTransformation shellNodeTransformation0 = new ShellNodeTransformation(rect0,
																							 Optional.of(parent0),
																							 rect1,
																							 Optional.of(parent1));
		final ShellNodeTransformation shellNodeTransformation1 = new ShellNodeTransformation(rect0,
																							 Optional.of(parent0),
																							 rect1,
																							 Optional.of(parent1));

		//when
		//both objects' hashcodes are compared
		final int hashCode0 = shellNodeTransformation0.hashCode();
		final int hashCode1 = shellNodeTransformation1.hashCode();

		//then
		//both objects hashcodes are the same
		assertEquals(hashCode0,
					 hashCode1);
	}
}
