/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.display.api;

/**
 * An <code>EventProducer</code> is a source of <code>DisplayEvent</code>s. A
 * <code>DisplayEvent</code> can be fetched by a call to
 * <code>getNextEvent</code>. An <code>EventProducter</code> can be registered
 * with a {@link DisplayServer}. The <code>Display</code> will fetch any events
 * and place them on the <code>Display</code>'s event queue. An
 * <code>EventProducer</code> can thus be seen as an "injector" of
 * <code>DisplayEvent</code>s.
 * <p>
 * Implementation notice:
 * <p>
 * A call to <code>getNextEvent</code> should block when no events are
 * available.
 */
public interface DisplayEventProducer {

	void startDisplayEventProduction();

	void stopDisplayEventProduction();
}
