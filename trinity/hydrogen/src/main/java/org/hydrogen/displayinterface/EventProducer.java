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
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hydrogen.displayinterface;

import org.hydrogen.displayinterface.event.DisplayEvent;

/**
 * An <code>EventProducer</code> is a source of <code>DisplayEvent</code>s. A
 * <code>DisplayEvent</code> can be fetched by a call to
 * <code>getNextEvent</code>. An <code>EventProducter</code> can be registered
 * with a {@link Display}. The <code>Display</code> will fetch any events and
 * place them on the <code>Display</code>'s event queue. An
 * <code>EventProducer</code> can thus be seen as an "injector" of
 * <code>DisplayEvent</code>s.
 * <p>
 * Implementation notice:
 * <p>
 * A call to <code>getNextEvent</code> should block when no events are
 * available.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface EventProducer {

	/**
	 * Returns the next <code>DisplayEvent</code> from this
	 * <code>EventProducer</code>'s <code>DisplayEvent</code> source.
	 * 
	 * @return The next {@link DisplayEvent} from this
	 *         <code>EventProducer</code>.
	 */
	DisplayEvent getNextEvent();
}
