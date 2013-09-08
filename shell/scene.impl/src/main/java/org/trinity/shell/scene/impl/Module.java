package org.trinity.shell.scene.impl;

import org.apache.onami.autobind.annotations.GuiceModule;
import org.trinity.shell.api.scene.manager.ShellLayoutManagerFactory;
import org.trinity.shell.api.scene.manager.ShellLayoutManagerLine;
import org.trinity.shell.scene.impl.manager.ShellLayoutManagerLineImpl;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 *
 */
@GuiceModule
public class Module extends AbstractModule {
	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(	ShellLayoutManagerLine.class,
														ShellLayoutManagerLineImpl.class)
				.build(ShellLayoutManagerFactory.class));
	}
}
