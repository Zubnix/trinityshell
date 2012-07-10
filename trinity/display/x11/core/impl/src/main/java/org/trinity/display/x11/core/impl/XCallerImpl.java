/*
 * This file is part of Fusion-X11. Fusion-X11 is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Fusion-X11 is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Fusion-X11. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.display.x11.core.impl;

import org.trinity.display.x11.core.api.XCall;
import org.trinity.display.x11.core.api.XCallExceptionHandler;
import org.trinity.display.x11.core.api.XCaller;
import org.trinity.display.x11.core.api.exception.BadXCall;

import com.google.inject.Inject;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
public final class XCallerImpl implements XCaller {
	/**
	 * 
	 */
	public final XCallExceptionHandler xCallExceptionHandler;

	/**
	 * Create a new <code>XNativeCaller</code> with the given
	 * <code>NativeErrorHandler</code>. The given
	 * <code>NativeErrorHandler</code> will be used to handle an erroneous
	 * <code>XNativeCall</code>.
	 * 
	 * @param xCallExceptionHandler
	 *            A {@link XCallExceptionHandler}.
	 */
	@Inject
	public XCallerImpl(final XCallExceptionHandler xCallExceptionHandler) {
		this.xCallExceptionHandler = xCallExceptionHandler;
	}

	/**
	 * Perform a X native call through the given <code>XNativeCall</code>. The
	 * given display peer and arguments are automatically passed to the given
	 * <code>XNativeCall</code>. The returned result is the same as
	 * {@link AbstractXcbCall#getResult()}.
	 * 
	 * @param <R>
	 *            A result type.
	 * @param <D>
	 *            A display peer type.
	 * @param <A>
	 *            An argument type.
	 * @param nativeCall
	 *            A {@link AbstractXcbCall}.
	 * @param xConnectionReference
	 *            A display peer.
	 * @param args
	 *            A number of arguments
	 * @return A result. @
	 * @throws BadXCall
	 */
	@Override
	public <R, D, A> R doCall(	final XCall<R, D, A> nativeCall,
								final D xConnectionReference,
								final A... args) {
		nativeCall.setConnectionReference(xConnectionReference);
		nativeCall.setArgs(args);
		if (nativeCall.exec()) {
			return this.xCallExceptionHandler
					.handleException(	this,
										nativeCall,
										xConnectionReference,
										args);
		} else {
			return nativeCall.getResult();
		}
	}
}
