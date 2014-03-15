package org.trinity.bootstrap;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import org.trinity.foundation.display.x11.impl.X11DefaultModule;
import org.trinity.shell.scene.impl.ShellSceneDefaultModule;

import javax.inject.Singleton;

@Module(
		injects = {
				EntryPoint.class,
				ObjectGraph.class
		},
		includes = {
				ShellSceneDefaultModule.class,
				X11DefaultModule.class,
				//BindingDefaultModule.class
		},
		complete = true,
		library = false
)
public class TrinityShellModule {

	private ObjectGraph objectGraph;

	void setObjectGraph(final ObjectGraph objectGraph) {
		this.objectGraph = objectGraph;
	}

	@Provides
	@Singleton
	ObjectGraph provideObjectGraph() {
		return this.objectGraph;
	}
}
