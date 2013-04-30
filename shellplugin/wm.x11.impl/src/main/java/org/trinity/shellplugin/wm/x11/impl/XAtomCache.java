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
package org.trinity.shellplugin.wm.x11.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.annotation.concurrent.ThreadSafe;

import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_intern_atom_cookie_t;
import org.freedesktop.xcb.xcb_intern_atom_reply_t;

import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;
import static java.lang.String.format;
import static org.freedesktop.xcb.LibXcb.xcb_intern_atom;
import static org.freedesktop.xcb.LibXcb.xcb_intern_atom_reply;

import static com.google.common.util.concurrent.Futures.transform;

@Bind(to = @To(Type.IMPLEMENTATION))
@Singleton
@ThreadSafe
public class XAtomCache {

	private final Map<Integer, String> atomNameCodes = new HashMap<Integer, String>();
	private final Map<String, Integer> atomCodeNames = new HashMap<String, Integer>();

	private final ListeningExecutorService wmExecutor;
	private final XConnection xConnection;

	@Inject
	XAtomCache(	final XConnection xConnection,
				final ListeningExecutorService wmExecutor) {
		this.xConnection = xConnection;
		this.wmExecutor = wmExecutor;
	}

	public ListenableFuture<Integer> internAtom(final String atomName) {

		final ListenableFuture<Integer> atomCacheCheckFuture = this.wmExecutor.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				return XAtomCache.this.atomCodeNames.get(atomName);
			}
		});

		final ListenableFuture<Integer> atomIdFuture = transform(	atomCacheCheckFuture,
																	new AsyncFunction<Integer, Integer>() {

																		@Override
																		public ListenableFuture<Integer> apply(final Integer atomId) {

																			if (atomId != null) {
																				return MoreExecutors
																						.sameThreadExecutor()
																						.submit(new Callable<Integer>() {
																							@Override
																							public Integer call()
																									throws Exception {
																								return atomId;
																							}
																						});
																			}

																			final xcb_intern_atom_cookie_t cookie_t = xcb_intern_atom(	XAtomCache.this.xConnection
																																				.getConnectionRef(),
																																		(short) 0,
																																		atomName.length(),
																																		atomName);

																			return XAtomCache.this.wmExecutor
																					.submit(new Callable<Integer>() {
																						@Override
																						public Integer call()
																								throws Exception {
																							final Integer internedAtomId = Integer
																									.valueOf(internAtomCall(cookie_t,
																															atomName));

																							XAtomCache.this.atomCodeNames
																									.put(	atomName,
																											internedAtomId);
																							XAtomCache.this.atomNameCodes
																									.put(	internedAtomId,
																											atomName);
																							return internedAtomId;
																						}
																					});
																		}
																	});

		return atomIdFuture;
	}

	private int internAtomCall(	final xcb_intern_atom_cookie_t cookie_t,
								final String atomName) {

		final xcb_generic_error_t e = new xcb_generic_error_t();
		final xcb_intern_atom_reply_t reply_t = xcb_intern_atom_reply(	this.xConnection.getConnectionRef(),
																		cookie_t,
																		e);
		if (xcb_generic_error_t.getCPtr(e) != 0) {
			throw new RuntimeException("xcb error");
		}
		final int atomId = reply_t.getAtom();
		return atomId;
	}

	public ListenableFuture<String> getAtomName(final int atomId) {
		return this.wmExecutor.submit(new Callable<String>() {
			@Override
			public String call() {

				final String atomName = XAtomCache.this.atomNameCodes.get(Integer.valueOf(atomId));
				if (atomName == null) {
					throw new NullPointerException(format(	"Atom with id %d was not interned.",
															atomId));
				}
				return atomName;
			}
		});
	}
}
