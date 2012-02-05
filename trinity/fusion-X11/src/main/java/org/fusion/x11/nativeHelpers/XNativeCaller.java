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
 * An <code>XNativeCaller</code> passes through any given arguments to a
 * provided <code>XNativeCall</code> and executes it. The
 * <code>XNativeCaller</code> will delegate the given <code>XNativeCall</code>
 * to a defined <code>NativeErrorHandler</code> should the given
 * <code>XNativeCall</code> return in an erroneous state.
 * <p>
 * The calling of a <code>XNativeCall</code> should be done like this:
 * {@link XNativeCaller#doNativeCall(XNativeCall, Object, Object...)}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class XNativeCaller {
	/**
	 * 
	 */
	public final NativeErrorHandler nativeErrorHandler;

	/**
	 * Create a new <code>XNativeCaller</code> with the given
	 * <code>NativeErrorHandler</code>. The given
	 * <code>NativeErrorHandler</code> will be used to handle an erroneous
	 * <code>XNativeCall</code>.
	 * 
	 * @param nativeErrorHandler
	 *            A {@link NativeErrorHandler}.
	 */
	public XNativeCaller(final NativeErrorHandler nativeErrorHandler) {
		this.nativeErrorHandler = nativeErrorHandler;
	}

	/**
	 * Perform a X native call through the given <code>XNativeCall</code>. The
	 * given display peer and arguments are automatically passed to the given
	 * <code>XNativeCall</code>. The returned result is the same as
	 * {@link XNativeCall#getResult()}.
	 * 
	 * @param <R>
	 *            A result type.
	 * @param <D>
	 *            A display peer type.
	 * @param <A>
	 *            An argument type.
	 * @param nativeCall
	 *            A {@link XNativeCall}.
	 * @param displayPeer
	 *            A display peer.
	 * @param args
	 *            A number of arguments
	 * @return A result. @
	 * @throws BadNativeXCall
	 */
	public <R, D, A> R doNativeCall(final XNativeCall<R, D, A> nativeCall,
			final D displayPeer, final A... args) {
		nativeCall.setDisplayPeer(displayPeer);
		nativeCall.setArgs(args);
		if (nativeCall.exec()) {
			return XNativeCaller.this.nativeErrorHandler.handleNativeError(
					XNativeCaller.this, nativeCall, displayPeer, args);
		} else {
			return nativeCall.getResult();

		}
	}
}
