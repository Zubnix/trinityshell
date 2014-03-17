package org.trinity.shell.scene.impl;

import dagger.Module;
import dagger.Provides;
import org.trinity.shell.scene.api.ShellSurfaceFactory;

@Module(
		injects = {
			ShellSurfaceDefaultFactory.class
		},
		complete = true,
		library = true
)
public class ShellSceneDefaultModule {
	@Provides
	ShellSurfaceFactory shellSurfaceFactory(final ShellSurfaceDefaultFactory shellSurfaceDefaultFactory) {
		return shellSurfaceDefaultFactory;
	}
}
