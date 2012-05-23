package org.hyperdrive.widget.impl;

import org.hyperdrive.widget.api.Button;
import org.hyperdrive.widget.api.CloseButton;
import org.hyperdrive.widget.api.DragButton;
import org.hyperdrive.widget.api.HideButton;
import org.hyperdrive.widget.api.KeyDrivenMenu;
import org.hyperdrive.widget.api.Label;
import org.hyperdrive.widget.api.MaximizeButton;
import org.hyperdrive.widget.api.Root;
import org.hyperdrive.widget.api.VirtualRoot;
import org.hyperdrive.widget.api.Widget;

import com.google.inject.AbstractModule;

public class WidgetModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Widget.class).to(BaseWidget.class);
		bind(Button.class).to(BaseButton.class);
		bind(CloseButton.class).to(BaseCloseButton.class);
		bind(DragButton.class).to(BaseDragButton.class);
		bind(HideButton.class).to(BaseHideButton.class);
		bind(Label.class).to(BaseLabel.class);
		bind(MaximizeButton.class).to(BaseMaximizeButton.class);
		bind(Root.class).to(BaseRoot.class);
		bind(VirtualRoot.class).to(BaseVirtualRoot.class);
		bind(KeyDrivenMenu.class).to(KeyDrivenAppLauncher.class);
		bind(DragButton.class).to(BaseResizeButton.class);
	}
}