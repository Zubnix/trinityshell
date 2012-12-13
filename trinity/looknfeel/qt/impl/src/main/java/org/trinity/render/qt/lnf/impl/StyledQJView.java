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
package org.trinity.render.qt.lnf.impl;

import javax.inject.Named;

import org.trinity.render.qt.api.QJPaintContext;
import org.trinity.shellplugin.widget.api.binding.ViewPropertySlot;

import com.trolltech.qt.gui.QFrame;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Named("StyledView")
public class StyledQJView extends QFrame {

	@ViewPropertySlot("name")
	public void setObjectName(	final QJPaintContext paintContext,
								final String name) {
		setObjectName(name);
		style().unpolish(this);
		ensurePolished();
	}
}