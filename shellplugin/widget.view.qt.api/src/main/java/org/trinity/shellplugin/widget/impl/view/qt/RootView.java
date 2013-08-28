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

package org.trinity.shellplugin.widget.impl.view.qt;

import org.trinity.foundation.api.render.binding.view.ObservableCollection;

import com.trolltech.qt.core.QChildEvent;
import com.trolltech.qt.core.QMargins;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;

class RootView extends QFrame {

	QFrame topBarView = new QFrame() {

		@ObservableCollection(value = "clientsBar", view = ClientBarElementView.class)
		QFrame clientsBarView = new QFrame() {

			QHBoxLayout clientsBarLayout = new QHBoxLayout(this);
			{
				// workaround for jambi css bug
				setObjectName("ClientsBar");
				this.clientsBarLayout.setContentsMargins(new QMargins(	0,
																		0,
																		0,
																		0));
				setLayout(this.clientsBarLayout);
			}

			@Override
			public void childEvent(final QChildEvent childEvent) {
				final QObject child = childEvent.child();
				if (childEvent.added() && child.isWidgetType()) {
					this.clientsBarLayout.addWidget((QWidget) child);
				}
			}
		};
		@ObservableCollection(value = "notificationsBar", view = NotificationsBarElementView.class)
		QFrame notificationsBarView = new QFrame() {

			QHBoxLayout notificationsBarLayout = new QHBoxLayout(this);
			{
				// workaround for jambi css bug
				setObjectName("NotificationsBar");
				this.notificationsBarLayout.setContentsMargins(new QMargins(0,
																			0,
																			0,
																			0));
				setLayout(this.notificationsBarLayout);
			}

			@Override
			public void childEvent(final QChildEvent childEvent) {
				final QObject child = childEvent.child();
				if (childEvent.added() && child.isWidgetType()) {
					this.notificationsBarLayout.addWidget((QWidget) child);
				}
			}
		};
		QHBoxLayout topBarLayout = new QHBoxLayout(this);
		{
			// workaround for jambi css bug
			setObjectName("TopBar");
			this.topBarLayout.setContentsMargins(new QMargins(	0,
																0,
																0,
																0));
			setLayout(this.topBarLayout);
			this.topBarLayout.addWidget(this.clientsBarView);
			this.topBarLayout.addWidget(this.notificationsBarView);
		}
	};
	@ObservableCollection(value = "bottomBar", view = ClientBarElementView.class)
	QFrame bottomBarView = new QFrame() {

		QHBoxLayout bottomBarLayout = new QHBoxLayout(this);
		{
			// workaround for jambi css bug
			setObjectName("BottomBar");
			this.bottomBarLayout.setContentsMargins(new QMargins(	0,
																	0,
																	0,
																	0));
			setLayout(this.bottomBarLayout);
		}

		@Override
		public void childEvent(final QChildEvent childEvent) {
			final QObject child = childEvent.child();
			if (childEvent.added() && child.isWidgetType()) {
				this.bottomBarLayout.addWidget((QWidget) child);
			}
		}
	};
	QVBoxLayout rootLayout = new QVBoxLayout(this);
	{
		// workaround for jambi css bug
		setObjectName(getClass().getSimpleName());
		this.rootLayout.setContentsMargins(new QMargins(0,
														0,
														0,
														0));
		this.rootLayout.addWidget(this.topBarView);
		this.rootLayout.addStretch();
		this.rootLayout.addWidget(this.bottomBarView);

		setLayout(this.rootLayout);

        showFullScreen();
		show();
	}
}
