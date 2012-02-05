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
 * Provides the tools to manage a tree of
 * {@link org.hyperdrive.geo.GeoTransformableRectangle}s. These rectangles can
 * form parent-child relations where children can delegate their geometry
 * changes/request to their parent rectangle. A parent rectangle can either act
 * on these child requests or, in turn, delegate them further up to their
 * respective parent. When a change is executed, the rectangle that will undergo 
 * the change will delegate this change to its
 * {@link org.hyperdrive.geo.GeoExecutor}.
 * <p>
 * A typical geometry change goes in the form of:
 * <ol>
 * <li>Set value(s).</li>
 * <li>Request change.</li>
 * <li>Find change approval authority</li>
 * <li>execute/deny change</li>
 * </ol>
 * <p>
 * The change approval authority is usually a
 * {@link org.hyperdrive.geo.GeoManager}. A <code>GeoManager</code> implements a
 * layout and decides whether or not the child request is valid or not. A
 * <code>GeoManager</code> can also decide to transform the geometry request and
 * execute the request in its mutated form.
 * <p>
 * The change approval authority is found by traversing the tree upward until a
 * parent with a <code>GeoManager</code> is found. If no <code>GeoManager</code>
 * can be found the request is directly executed without any interference.
 * <p>
 * The geometry package makes heavy use of the event system. Each geometry
 * request is implemented in the form of a {@link org.hyperdrive.geo.GeoEvent}.
 * <p>
 * The following geometry operations are supplied by the geometry package:
 * <ul>
 * <li>LOWER</li>
 * <li>MOVE</li>
 * <li>RESIZE</li>
 * <li>MOVE+RESIZE</li>
 * <li>RAISE</li>
 * <li>REPARENT</li>
 * <li>VISIBILITY</li>
 * <li>DESTROY</li>
 * </ul>
 * Each of these operations comes in two fold: As request and as confirmation as
 * described in the <code>GeoEvent</code> class. Each geometric request is
 * described by a {@link org.hyperdrive.geo.GeoTransformation}. A
 * <code>GeoTransformation</code> describes both the current state as the
 * desired state of the request.
 * <p>
 * The geometry package provides a {@link org.hyperdrive.geo.GeoVirtRectangle}
 * as a non visual "virtual" implementation of the
 * {@link org.hyperdrive.geo.GeoTransformableRectangle}. Other systems or
 * mechanisms wishing to make use of the geometry package should extend from the
 * <code>GeoTransformableRectangle</code> and implement their own
 * <code>GeoExecutor</code>. The most notable examples being
 * {@link org.hyperdrive.core.ClientWindow} +
 * {@link org.hyperdrive.core.RenderAreaGeoExecutor} and
 * {@link org.hyperdrive.widget.Widget} +
 * {@link org.hyperdrive.widget.WidgetGeoExecutor}
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
package org.hyperdrive.geo;