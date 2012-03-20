package org.hypercube.configuration;

import org.hypercube.hyperwidget.ClientContainer;
import org.hypercube.view.fusionqtjambi.ButtonView;
import org.hypercube.view.fusionqtjambi.ClientContainerView;
import org.hypercube.view.fusionqtjambi.ClientManagerLabelView;
import org.hypercube.view.fusionqtjambi.KeyDrivenMenuView;
import org.hypercube.view.fusionqtjambi.LabelView;
import org.hypercube.view.fusionqtjambi.RealRootView;
import org.hypercube.view.fusionqtjambi.WidgetView;
import org.hyperdrive.api.widget.ViewBinder;
import org.hyperdrive.widget.BaseButton;
import org.hyperdrive.widget.BaseClientManager;
import org.hyperdrive.widget.BaseClientManager.ClientManagerLabel;
import org.hyperdrive.widget.ClientNameLabel;
import org.hyperdrive.widget.BaseCloseButton;
import org.hyperdrive.widget.BaseDragButton;
import org.hyperdrive.widget.BaseHideButton;
import org.hyperdrive.widget.KeyDrivenAppLauncher;
import org.hyperdrive.widget.BaseKeyDrivenMenu;
import org.hyperdrive.widget.BaseLabel;
import org.hyperdrive.widget.BaseMaximizeButton;
import org.hyperdrive.widget.BaseRoot;
import org.hyperdrive.widget.ResizeButton;
import org.hyperdrive.widget.BaseVirtualRoot;
import org.hyperdrive.widget.BaseWidget;

public class ViewBindingsConfigPerform implements Runnable {
	@Override
	public void run() {
		ViewBinder.bindView(Button.View.class, ButtonView.class);
		ViewBinder.bindView(ClientContainer.View.class,
				ClientContainerView.class);
		ViewBinder.bindView(BaseClientManager.View.class, WidgetView.class);
		ViewBinder.bindView(ClientManagerLabel.View.class,
				ClientManagerLabelView.class);
		ViewBinder.bindView(ClientNameLabel.View.class, LabelView.class);
		ViewBinder.bindView(CloseButton.View.class, ButtonView.class);
		ViewBinder.bindView(BaseDragButton.View.class, ButtonView.class);
		ViewBinder.bindView(HideButton.View.class, ButtonView.class);
		ViewBinder.bindView(KeyDrivenAppLauncher.View.class,
				KeyDrivenMenuView.class);
		ViewBinder.bindView(BaseKeyDrivenMenu.View.class, KeyDrivenMenuView.class);
		ViewBinder.bindView(BaseLabel.View.class, LabelView.class);
		ViewBinder.bindView(BaseMaximizeButton.View.class, ButtonView.class);
		ViewBinder.bindView(BaseRoot.View.class, RealRootView.class);
		ViewBinder.bindView(BaseResizeButton.View.class, ButtonView.class);
		ViewBinder.bindView(VirtualRoot.View.class, WidgetView.class);
		ViewBinder.bindView(BaseWidget.View.class, WidgetView.class);
	}
}
