package org.trinity.shellplugin.wm.x11.impl;

import org.apache.onami.autobind.annotations.GuiceModule;
import org.trinity.shell.api.bindingkey.ShellRootNode;
import org.trinity.shell.api.scene.ShellNodeParent;
import org.trinity.shellplugin.wm.api.Desktop;
import org.trinity.shellplugin.wm.x11.impl.scene.ClientBarElement;
import org.trinity.shellplugin.wm.x11.impl.scene.ClientBarElementFactory;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.trinity.shellplugin.wm.x11.impl.scene.ShellRootWidget;

@GuiceModule
public class Module extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(	ClientBarElement.class,
														ClientBarElement.class).build(ClientBarElementFactory.class));
		bind(ShellRootWidget.class).asEagerSingleton();
		bind(ShellNodeParent.class).annotatedWith(ShellRootNode.class).to(ShellRootWidget.class);
		bind(Desktop.class).to(ShellRootWidget.class);
	}
}
