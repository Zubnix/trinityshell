package org.trinity.shell.widget.impl;

import org.trinity.shell.widget.api.Button;
import org.trinity.shell.widget.api.CloseButton;
import org.trinity.shell.widget.api.DragButton;
import org.trinity.shell.widget.api.HideButton;
import org.trinity.shell.widget.api.KeyDrivenMenu;
import org.trinity.shell.widget.api.Label;
import org.trinity.shell.widget.api.MaximizeButton;
import org.trinity.shell.widget.api.Root;
import org.trinity.shell.widget.api.VirtualRoot;
import org.trinity.shell.widget.api.Widget;

import com.google.inject.AbstractModule;

public class WidgetModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Widget.class).to(WidgetImpl.class);
		bind(Button.class).to(ButtonImpl.class);
		bind(CloseButton.class).to(CloseButtonImpl.class);
		bind(DragButton.class).to(DragButtonImpl.class);
		bind(HideButton.class).to(HideButtonImpl.class);
		bind(Label.class).to(LabelImpl.class);
		bind(MaximizeButton.class).to(MaximizeButtonImpl.class);
		bind(Root.class).to(RootImpl.class);
		bind(VirtualRoot.class).to(VirtualRootImpl.class);
		bind(KeyDrivenMenu.class).to(KeyDrivenAppLauncher.class);
		bind(DragButton.class).to(ResizeButtonImpl.class);
	}
}