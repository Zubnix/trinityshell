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
package org.trinity.shellplugin.wm.x11.impl.protocol;

import static java.lang.String.format;
import static org.freedesktop.xcb.LibXcb.xcb_intern_atom;
import static org.freedesktop.xcb.LibXcb.xcb_intern_atom_reply;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.annotation.concurrent.NotThreadSafe;

import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_intern_atom_cookie_t;
import org.freedesktop.xcb.xcb_intern_atom_reply_t;
import org.trinity.foundation.api.shared.OwnerThread;
import org.trinity.shellplugin.wm.x11.impl.XConnection;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(Type.IMPLEMENTATION))
@Singleton
@NotThreadSafe
@OwnerThread("WindowManager")
public class XAtomCache {

	private final Map<Integer, String> atomNameCodes = new HashMap<Integer, String>();
	private final Cache<String, Integer> atomCodeNames = CacheBuilder.newBuilder().concurrencyLevel(1).build();

	private final XConnection xConnection;

	@Inject
	XAtomCache(final XConnection xConnection) {
		this.xConnection = xConnection;
		internOftenUsedAtoms();
	}

	private void internOftenUsedAtoms() {
		getAtom("UTF8");
		getAtom("WM_NAME");
		getAtom("WM_PROTOCOLS");
		getAtom("WM_STATE");
	}

	public int getAtom(final String atomName) {

		try {
			return this.atomCodeNames.get(	atomName,
											new Callable<Integer>() {
												@Override
												public Integer call() {
													final Integer atomId = internAtom(atomName);
													XAtomCache.this.atomNameCodes.put(	atomId,
																						atomName);
													return atomId;
												}
											});
		} catch (final ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	private Integer internAtom(final String atomName) {
		final xcb_intern_atom_cookie_t cookie_t = xcb_intern_atom(	XAtomCache.this.xConnection.getConnectionRef(),
																	(short) 0,
																	atomName.length(),
																	atomName);

		final xcb_generic_error_t e = new xcb_generic_error_t();
		final xcb_intern_atom_reply_t reply_t = xcb_intern_atom_reply(	this.xConnection.getConnectionRef(),
																		cookie_t,
																		e);
		if (xcb_generic_error_t.getCPtr(e) != 0) {
			throw new RuntimeException("xcb error");
		}
		final int atomId = reply_t.getAtom();
		return Integer.valueOf(atomId);
	}

	public String getAtomName(final int atomId) {

		final String atomName = this.atomNameCodes.get(Integer.valueOf(atomId));
		if (atomName == null) {
			throw new NullPointerException(format(	"Atom with id %d was not interned.",
													atomId));
		}
		return atomName;
	}
}
