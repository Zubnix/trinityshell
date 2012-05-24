///*
// * This file is part of Fusion-X11.
// * 
// * Fusion-X11 is free software: you can redistribute it and/or modify it under
// * the terms of the GNU General Public License as published by the Free Software
// * Foundation, either version 3 of the License, or (at your option) any later
// * version.
// * 
// * Fusion-X11 is distributed in the hope that it will be useful, but WITHOUT ANY
// * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
// * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
// * 
// * You should have received a copy of the GNU General Public License along with
// * Fusion-X11. If not, see <http://www.gnu.org/licenses/>.
// */
//package org.fusion.x11.core;
//
///**
// * 
// * @author Erik De Rijcke
// * @since 1.0
// * 
// */
//public class XResourceEvent extends FusionEvent {
//
//	public static final FusionEventType NEW_RESOURCE = new FusionEventType();
//	public static final FusionEventType NEW_CLIENT = new FusionEventType();
//
//	private final XResource xResource;
//
//	/**
//	 * 
//	 * @param type
//	 * @param xResource
//	 */
//	public XResourceEvent(final FusionEventType type, final XResource xResource) {
//		super(type);
//		this.xResource = xResource;
//	}
//
//	/**
//	 * 
//	 * @return
//	 */
//	public XResource getxResource() {
//		return this.xResource;
//	}
// }