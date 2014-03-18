package org.trinity.x11.defaul.shell;

import dagger.Module;

@Module(
		injects = {
                DefaultShellSurfaceFactory.class
		},
		complete = true,
		library = true
)
public class SimpleShellSceneModule {
}
