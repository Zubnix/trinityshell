package org.trinity.bootstrap;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import org.trinity.foundation.api.binding.binding.RenderBindingImplModule;
import org.trinity.foundation.display.x11.impl.X11DefaultModule;

import javax.inject.Singleton;

@Module(
		injects = {
				EntryPoint.class,
				ObjectGraph.class
		},
		includes = {
				X11DefaultModule.class,
                RenderBindingImplModule.class
		},
		complete = true,
		library = false
)
public class TrinityShellModule {

	private ObjectGraph objectGraph;

	void setObjectGraph(final ObjectGraph objectGraph) {
		this.objectGraph = objectGraph;
	}

	@Provides @Singleton ObjectGraph provideObjectGraph(){
		return this.objectGraph;
	}
}
