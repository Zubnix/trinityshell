package org.hypercube.configuration;

import org.hypercube.view.fusionqtjambi.ButtonView;
import org.hypercube.view.fusionqtjambi.ClientContainerView;
import org.hypercube.view.fusionqtjambi.ClientManagerLabelView;
import org.hypercube.view.fusionqtjambi.KeyDrivenMenuView;
import org.hypercube.view.fusionqtjambi.LabelView;
import org.hypercube.view.fusionqtjambi.RealRootView;
import org.hypercube.view.fusionqtjambi.WidgetView;
import org.hyperdrive.widget.api.ViewBinder;

public class ViewBindingsConfigPerform implements Runnable {
	@Override
	public void run() {
		ViewBinder.get().bind(ButtonView.class);
		ViewBinder.get().bind(ClientContainerView.class);
		ViewBinder.get().bind(WidgetView.class);
		ViewBinder.get().bind(ClientManagerLabelView.class);
		ViewBinder.get().bind(LabelView.class);
		ViewBinder.get().bind(ButtonView.class);
		ViewBinder.get().bind(ButtonView.class);
		ViewBinder.get().bind(ButtonView.class);
		ViewBinder.get().bind(KeyDrivenMenuView.class);
		ViewBinder.get().bind(KeyDrivenMenuView.class);
		ViewBinder.get().bind(LabelView.class);
		ViewBinder.get().bind(ButtonView.class);
		ViewBinder.get().bind(RealRootView.class);
		ViewBinder.get().bind(ButtonView.class);
		ViewBinder.get().bind(WidgetView.class);
		ViewBinder.get().bind(WidgetView.class);
	}
}
