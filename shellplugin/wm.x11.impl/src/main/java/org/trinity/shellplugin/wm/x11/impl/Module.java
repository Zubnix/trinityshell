package org.trinity.shellplugin.wm.x11.impl;

import org.apache.onami.autobind.annotations.GuiceModule;
import org.trinity.shellplugin.wm.x11.impl.scene.ClientBarElement;
import org.trinity.shellplugin.wm.x11.impl.scene.ClientBarElementFactory;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

@GuiceModule
public class Module extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(	ClientBarElement.class,
														ClientBarElement.class).build(ClientBarElementFactory.class));
	}
}
