/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package org.trinity.shellplugin.wm.x11.impl.protocol;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_intern_atom_cookie_t;
import org.freedesktop.xcb.xcb_intern_atom_reply_t;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.display.x11.api.XConnection;

import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.lang.String.format;
import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;
import static org.freedesktop.xcb.LibXcb.xcb_intern_atom;
import static org.freedesktop.xcb.LibXcb.xcb_intern_atom_reply;

@Bind(to = @To(IMPLEMENTATION))
@Singleton
@NotThreadSafe
@ExecutionContext(DisplayExecutor.class)
public class XAtomCache {

	private final Map<Integer, String> atomNameCodes = new HashMap<>();
	private final Cache<String, Integer> atomCodeNames = CacheBuilder.newBuilder().concurrencyLevel(1).build();
	private final XConnection xConnection;

	@Inject
	XAtomCache(final XConnection xConnection) {
		this.xConnection = xConnection;
	}

	public int getAtom(final String atomName) {

		try {
			return this.atomCodeNames.get(	atomName,
                                              () -> {
                                                  final Integer atomId = internAtom(atomName);
                                                  XAtomCache.this.atomNameCodes.put(atomId,
                                                                                    atomName);
                                                  return atomId;
                                              });
        } catch (final ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	private Integer internAtom(final String atomName) {
		final xcb_intern_atom_cookie_t cookie_t = xcb_intern_atom(	XAtomCache.this.xConnection
																			.getConnectionReference(),
																	(short) 0,
																	atomName.length(),
																	atomName);

		final xcb_generic_error_t e = new xcb_generic_error_t();
		final xcb_intern_atom_reply_t reply_t = xcb_intern_atom_reply(	this.xConnection.getConnectionReference(),
																		cookie_t,
																		e);
		if (xcb_generic_error_t.getCPtr(e) != 0) {
			throw new RuntimeException("xcb error");
		}
		return reply_t.getAtom();
	}

	public String getAtomName(final int atomId) {

		final String atomName = this.atomNameCodes.get(atomId);
		if (atomName == null) {
			throw new NullPointerException(format(	"Atom with id %d was not interned.",
													atomId));
		}
		return atomName;
	}
}
