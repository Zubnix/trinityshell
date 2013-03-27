package org.trinity.shellplugin.wm.impl.view;

import org.trinity.foundation.api.render.binding.view.ObservableCollection;

import com.trolltech.qt.core.QChildEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;

class RootView extends QFrame {

	@ObservableCollection(value = "topBar", view = BarItemView.class)
	BarView topBarView = new BarView(this) {
		QHBoxLayout topBarLayout = new QHBoxLayout(RootView.this.topBarView);
		{
			setLayout(this.topBarLayout);
			setSizePolicy(new QSizePolicy(	QSizePolicy.Policy.Maximum,
											QSizePolicy.Policy.Fixed));
		}

		@Override
		public void childEvent(final QChildEvent childEvent) {

			final QObject child = childEvent.child();
			if (childEvent.added() && child.isWidgetType()) {
				this.topBarLayout.addWidget((QWidget) child);
			}
		}
	};

	@ObservableCollection(value = "bottomBar", view = BarItemView.class)
	BarView bottomBarView = new BarView(this) {

		QHBoxLayout bottomBarLayout = new QHBoxLayout(RootView.this.bottomBarView);

		{
			setLayout(this.bottomBarLayout);
			setSizePolicy(new QSizePolicy(	QSizePolicy.Policy.Maximum,
											QSizePolicy.Policy.Fixed));
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
		setLayout(this.rootLayout);
		this.rootLayout.addWidget(this.topBarView);
		this.rootLayout.addStretch();
		this.rootLayout.addWidget(this.bottomBarView);

		this.topBarView.show();
		this.bottomBarView.show();
	}
}