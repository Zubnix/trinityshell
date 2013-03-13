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
package org.trinity.foundation.display.x11.impl;


// TODO move this class to an XshellPlugin
// @Bind(to = @To(Type.IMPLEMENTATION))
// @Singleton
// @NotThreadSafe
// public class XAtomCache {
//
// private final XConnection xConnection;
//
// private final Map<Integer, String> atomNameCodes = new HashMap<Integer,
// String>();
// private final Map<String, Integer> atomCodeNames = new HashMap<String,
// Integer>();
//
// @Inject
// XAtomCache(final XConnection xConnection) {
// this.xConnection = xConnection;
// }
//
// public int getAtom(final String atomName) {
// synchronized (this.atomCodeNames) {
// synchronized (this.atomNameCodes) {
// Integer atomCode = this.atomCodeNames.get(atomName);
// if (atomCode == null) {
// atomCode = Integer.valueOf(internAtom(atomName));
// this.atomCodeNames.put( atomName,
// atomCode);
// this.atomNameCodes.put( atomCode,
// atomName);
// }
// return atomCode;
// }
// }
// }
//
// private int internAtom(final String atomName) {
// final xcb_intern_atom_cookie_t cookie_t = LibXcb.xcb_intern_atom(
// this.xConnection.getConnectionReference(),
// (short) 0,
// atomName.length(),
// atomName);
// final xcb_generic_error_t e = new xcb_generic_error_t();
// final xcb_intern_atom_reply_t reply_t = LibXcb.xcb_intern_atom_reply(
// this.xConnection
// .getConnectionReference(),
// cookie_t,
// e);
// if (xcb_generic_error_t.getCPtr(e) != 0) {
// throw new RuntimeException("xcb error");
// }
// final int atomId = reply_t.getAtom();
// return atomId;
// }
//
// public String getAtom(final int atomId) {
// synchronized (this.atomCodeNames) {
// synchronized (this.atomNameCodes) {
// return this.atomNameCodes.get(Integer.valueOf(atomId));
// }
// }
// }
//
// }
