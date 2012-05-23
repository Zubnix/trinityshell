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

/**
 * Provides a number of 
 * interfaces that together describe how interaction with a paint back-end should 
 * take place. A paint back-end indirectly provies access to a paint toolkit like 
 * Swing or QtJambi, depending on the paint back-end's implementation. This means 
 * that the functionality of the paint back-end is largely dependend on the paint 
 * toolkit that is used. The <code>paintinterface</code> package itself does not 
 * define how a paint back-end is implemented but expects certain functionality. 
 * A paint back-end should run in a seperate gui thread for optimal performance.
 * <p> 
 * The following functionality should be provided by the paint back-end:
 * <ul>
 * <li>Be able to create a visual representation of a <code>Paintable</code>. This is usally
 * called a 'widget' or 'component' by the paint toolkit. In the <code>paintinterface</code> 
 * package this is called a "paint peer".</li>
 * <li>Be able to return the native render area handle when
 * creating a new paint peer (see 
 * {@link org.trinity.core.render.api.Painter#initPaintPeer()}).</li>
 * <li>Be able to create or get a reference to the root window paint peer of the native 
 * windowing system.</li>
 * <li>Be able to listen for events of the paint toolkit and construct the corresponding
 * events defined in the <code>org.hydrogen.displayinterface.event</code> package. These events
 * should be placed on the event queue of the <code>Display</code> object. (See {@link org.trinity.core.display.api.EventProducer})</li>
 * <li>Be able to enable+disable redirecting all keyboard input events of a paint peer.</li>
 * <li>Be able to enable+disbable redirecting all mouse input events of a paint peer.</li>
 * <li>Be able to change the geometry of a paint peer.</li>
 * <li>Be able to destroy a paint peer.</li>
 * <li>Be able to change the parent (reparent) a paint peer.</li>
 * <li>Be able to give the input focus to a paint peer.</li>
 * </ul>
 * <p>
 * These requirements are also indirectly found in the <code>Painter</code> interface 
 * through its declared functions.
 * 
 * @author Erik De Rijcke
 * @version 0.0.1
 */
package org.trinity.core.render.api;