/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */

// TODO documentation
/**
 * Provides classes and interfaces that represent a desktop protocol. A desktop 
 * protocol defines how a client should be closed, how a client propagates it's 
 * name and preferences etc.
 * <p>
 * A {@link org.hyperdrive.foundation.impl.ClientWindow} can be notified of desktop protocol
 * events by registering it with the {@link org.trinity.shell.protocol.DesktopProtocol}.
 * <p>
 * A separate desktop specific implementation must be provided in order for the 
 * hyperdrive library to make use of a desktop specific protocol. 
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
package org.trinity.shell.protocol;