/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package org.trinity.shell.api.scene.manager;

import org.trinity.shell.api.scene.ShellNode;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.media.nativewindow.util.Insets;

/***************************************
 * Layout property to be used with a {@link ShellLayoutManagerLine}.
 *
 ***************************************
 */
@Immutable
public class ShellLayoutPropertyLine implements ShellLayoutProperty {

	private final int    weight;
	private final Insets margins;

	/***************************************
	 * Create a new {@code ShellLayoutPropertyLine} with the given weight and
	 * {@code Margins}. The weight will be used to calculate the child
	 * {@link ShellNode} dimensions, relative to the weight of the other child
	 * {@code ShellNode}s. The {@code Margins} will be subtracted from the
	 * calculated dimensions of the child.
	 *
	 * @param weight
	 *            a weight, 0 for a statically sized child.
	 * @param margins
	 *            {@link Insets}
	 ***************************************
	 */
	public ShellLayoutPropertyLine(final int weight,
								   @Nonnull final Insets margins) {
		this.weight = weight;
		this.margins = margins;
	}

	/***************************************
	 * Create a new {@code ShellLayoutPropertyLine} with the given weight and no
	 * {@code Margins}. Short for
	 * {@code new ShellLayoutPropertyLine(weight, Margins.NO_MARGINS)}.
	 *
	 * @param weight
	 *            a weight, 0 for a statically sized child.
	 * @see #ShellLayoutPropertyLine(int, Insets)
	 ***************************************
	 */
	public ShellLayoutPropertyLine(final int weight) {
		this(weight,
			 new Insets(0,
						0,
						0,
						0));
	}

	/***************************************
	 * The layout weight given to a child {@code ShellNode}.
	 *
	 * @return a weight, 0 for a statically sized child.
	 ***************************************
	 */
	public int getWeight() {
		return this.weight;
	}

	/***************************************
	 * The {@code Margins} that will be subtracted from the calculated
	 * dimensions of the child {@code ShellNode}.
	 *
	 * @return {@link Insets}
	 ***************************************
	 */
	public Insets getMargins() {
		return this.margins;
	}
}