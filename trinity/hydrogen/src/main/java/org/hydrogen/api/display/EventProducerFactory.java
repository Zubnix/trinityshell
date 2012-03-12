/*
 * This file is part of Hydrogen.
 * 
 * Hydrogen is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hydrogen is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hydrogen. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hydrogen.api.display;

/**
 * An <code>EventProducerFactory</code> constructs new
 * <code>EventProducer</code>s for a given <code>Display</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface EventProducerFactory {
	/**
	 * Constructs a new <code>EventProducer</code> for a given
	 * <code>Display</code>. The given <code>Display</code> should be a
	 * <code>Display</code> that wants to fetch <code>DisplayEvent</code>s from
	 * the returned <code>EventProducer</code>.
	 * 
	 * @param display
	 *            The <code>Display</code> that will fetch the newly created
	 *            <code>EventProducer</code>'s <code>DisplayEvent</code>s.
	 *            <p>
	 *            Notice that multiple <code>EventProducer</code>s can be active
	 *            for the given <code>Display</code>.
	 * 
	 * @return An <code>EventProducer</code>.
	 */
	EventProducer getNewEventProducer(Display display);

}
