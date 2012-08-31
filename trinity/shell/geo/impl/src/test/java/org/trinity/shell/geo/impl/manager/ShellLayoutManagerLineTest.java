package org.trinity.shell.geo.impl.manager;

import org.junit.Test;
import org.mockito.Mockito;
import org.trinity.foundation.shared.geometry.api.Margins;
import org.trinity.shell.geo.api.ShellNode;
import org.trinity.shell.geo.api.manager.ShellLayoutPropertyLine;

// TODO test corner cases with margins (eg widths that are less than margins,
// zero width sizes etc.)
public class ShellLayoutManagerLineTest {

	@Test
	public void horizontalResizeParentTest() {

		final ShellLayoutManagerLine shellLayoutManagerLine = new ShellLayoutManagerLine();

		final ShellNode parent = Mockito.mock(ShellNode.class);
		Mockito.when(parent.getWidth()).thenReturn(120);
		Mockito.when(parent.getHeight()).thenReturn(100);

		final ShellNode child0 = Mockito.mock(ShellNode.class);
		final ShellNode child1 = Mockito.mock(ShellNode.class);
		final ShellNode child2 = Mockito.mock(ShellNode.class);

		shellLayoutManagerLine.addChildNode(child0,
											new ShellLayoutPropertyLine(1,
																		new Margins(0)));
		shellLayoutManagerLine.addChildNode(child1,
											new ShellLayoutPropertyLine(1,
																		new Margins(0)));
		shellLayoutManagerLine.addChildNode(child2,
											new ShellLayoutPropertyLine(1,
																		new Margins(0)));

		shellLayoutManagerLine.layout(parent);

		Mockito.verify(child0).setWidth(40);
		Mockito.verify(child1).setWidth(40);
		Mockito.verify(child1).setWidth(40);

	}

	@Test
	public void horizontalResizeChildTest() {
		// TODO
		throw new RuntimeException("Not yet implemented");
	}

	@Test
	public void horizontalInverseResizeParentTest() {
		// TODO
		throw new RuntimeException("Not yet implemented");
	}

	@Test
	public void horizontalInverseResizeChildTest() {
		// TODO
		throw new RuntimeException("Not yet implemented");
	}

	@Test
	public void verticalResizeParentTest() {
		// TODO
		throw new RuntimeException("Not yet implemented");
	}

	@Test
	public void verticalResizeChildTest() {
		// TODO
		throw new RuntimeException("Not yet implemented");
	}

	@Test
	public void verticalInverseResizeParentTest() {
		// TODO
		throw new RuntimeException("Not yet implemented");
	}

	@Test
	public void verticalInverseResizeChildTest() {
		// TODO
		throw new RuntimeException("Not yet implemented");
	}
}
