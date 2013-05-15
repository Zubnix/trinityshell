package org.trinity.shellplugin.wm.x11.impl.view;

import org.trinity.foundation.api.render.binding.view.ObservableCollection;

import com.trolltech.qt.core.QChildEvent;
import com.trolltech.qt.core.QMargins;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;

class RootView extends QFrame {

	@ObservableCollection(value = "notificationsBar", view = NotificationBarElementView.class)
	QFrame notificationsBarView = new QFrame() {
		QHBoxLayout notificationBarLayout = new QHBoxLayout(RootView.this.clientsBarView);

		{
			// workaround for jambi css bug
			setObjectName("NotificationsBar");

			this.notificationBarLayout.setContentsMargins(new QMargins(	0,
																		0,
																		0,
																		0));
			setLayout(this.notificationBarLayout);
		}

		@Override
		public void childEvent(final QChildEvent childEvent) {
			final QObject child = childEvent.child();
			if (childEvent.added() && child.isWidgetType()) {
				this.notificationBarLayout.addWidget((QWidget) child);
			}
		}
	};

	@ObservableCollection(value = "clientsBar", view = ClientBarElementView.class)
	QFrame clientsBarView = new QFrame() {

		QHBoxLayout clientsBarLayout = new QHBoxLayout(RootView.this.clientsBarView);

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

	QFrame topBarView = new QFrame() {

		QHBoxLayout topBarLayout = new QHBoxLayout(RootView.this.topBarView);

		{
			// workaround for jambi css bug
			setObjectName("TopBar");

			this.topBarLayout.setContentsMargins(new QMargins(	0,
																0,
																0,
																0));
			setLayout(this.topBarLayout);
			this.topBarLayout.addWidget(RootView.this.clientsBarView);
			this.topBarLayout.addWidget(RootView.this.notificationsBarView);
		}

	};

	@ObservableCollection(value = "bottomBar", view = ClientBarElementView.class)
	QFrame bottomBarView = new QFrame() {

		QHBoxLayout bottomBarLayout = new QHBoxLayout(RootView.this.bottomBarView);

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
	}
}