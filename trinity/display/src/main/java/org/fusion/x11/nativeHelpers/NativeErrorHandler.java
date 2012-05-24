/*
 * This file is part of Fusion-X11.
 * 
 * Fusion-X11 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Fusion-X11 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-X11. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.x11.nativeHelpers;

/**
 * A <code>NativeBufferHelper</code> is responsible for handling an erroneous
 * <code>XNativeCall</code>. A <code>NativeErrorHandler</code> does this by
 * implementing the
 * <code>handleNativeError(XNativeCaller,XNativeCall,D,A...)</code> method. This
 * method will be called by a <code>XNativeCaller</code> when a
 * <code>XNativeCall</code> returns with an erroneous state.
 * <p>
 * A <code>NativeErrorHandler</code> is given to the constructor of a
 * <code>XNativeCaller</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface NativeErrorHandler {
	/**
	 * Handle a <code>XNativeCall</code> that did not complete correctly.
	 * <p>
	 * Given are the <code>XNativeCaller</code> that called the
	 * <code>handleNativeError</code> and the erroneous <code>XNativeCall</code>
	 * , the original erroneous <code>XNativeCall</code>, the display peer that
	 * was used in performing the given erroneous <code>XNativeCall</code> and
	 * the additional arguments used in performing the given erroneous
	 * <code>XNativeCall</code>.
	 * 
	 * @param <R>
	 *            The return type of the given erroneous
	 *            <code>XNativeCall</code>.
	 * @param <D>
	 *            The display peer type of the given erroneous
	 *            <code>XNativeCall</code>.
	 * @param <A>
	 *            The additional arguments type of the given erroneous
	 *            <code>XNativeCall</code>.
	 * @param xNativeCaller
	 *            An {@link XNativeCaller}.
	 * @param erroneousNativeCall
	 *            An erroneous {@link XNativeCall}.
	 * @param displayPeer
	 *            A display peer.
	 * @param args
	 *            Additional arguments.
	 * @return A new result.
	 * @throws BadNativeXCall
	 */
	<R, D, A> R handleNativeError(final XNativeCaller xNativeCaller,
			final XNativeCall<R, D, A> erroneousNativeCall,
			final D displayPeer, final A... args);
}
