package org.hypercube.configuration;

import org.hypercube.hyperwidget.ClientContainer;
import org.hypercube.view.fusionqtjambi.ButtonView;
import org.hypercube.view.fusionqtjambi.ClientContainerView;
import org.hypercube.view.fusionqtjambi.ClientManagerLabelView;
import org.hypercube.view.fusionqtjambi.KeyDrivenMenuView;
import org.hypercube.view.fusionqtjambi.LabelView;
import org.hypercube.view.fusionqtjambi.RealRootView;
import org.hypercube.view.fusionqtjambi.WidgetView;
import org.hyperdrive.widget.Button;
import org.hyperdrive.widget.ClientManager;
import org.hyperdrive.widget.ClientManager.ClientManagerLabel;
import org.hyperdrive.widget.ClientNameLabel;
import org.hyperdrive.widget.CloseButton;
import org.hyperdrive.widget.DragButton;
import org.hyperdrive.widget.HideButton;
import org.hyperdrive.widget.KeyDrivenAppLauncher;
import org.hyperdrive.widget.KeyDrivenMenu;
import org.hyperdrive.widget.Label;
import org.hyperdrive.widget.MaximizeButton;
import org.hyperdrive.widget.RealRoot;
import org.hyperdrive.widget.ResizeButton;
import org.hyperdrive.widget.ViewBinder;
import org.hyperdrive.widget.VirtualRoot;
import org.hyperdrive.widget.Widget;

public class ViewBindingsConfigPerform implements Runnable {
	@Override
	public void run() {
		ViewBinder.bindView(Button.View.class, ButtonView.class);
		ViewBinder.bindView(ClientContainer.View.class,
				ClientContainerView.class);
		ViewBinder.bindView(ClientManager.View.class, WidgetView.class);
		ViewBinder.bindView(ClientManagerLabel.View.class,
				ClientManagerLabelView.class);
		ViewBinder.bindView(ClientNameLabel.View.class, LabelView.class);
		ViewBinder.bindView(CloseButton.View.class, ButtonView.class);
		ViewBinder.bindView(DragButton.View.class, ButtonView.class);
		ViewBinder.bindView(HideButton.View.class, ButtonView.class);
		ViewBinder.bindView(KeyDrivenAppLauncher.View.class,
				KeyDrivenMenuView.class);
		ViewBinder.bindView(KeyDrivenMenu.View.class, KeyDrivenMenuView.class);
		ViewBinder.bindView(Label.View.class, LabelView.class);
		ViewBinder.bindView(MaximizeButton.View.class, ButtonView.class);
		ViewBinder.bindView(RealRoot.View.class, RealRootView.class);
		ViewBinder.bindView(ResizeButton.View.class, ButtonView.class);
		ViewBinder.bindView(VirtualRoot.View.class, WidgetView.class);
		ViewBinder.bindView(Widget.View.class, WidgetView.class);
	}
}
