package org.trinity.bootstrap;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import org.trinity.wayland.platform.newt.PlatformNewtModule;
import org.trinity.wayland.protocol.WlProtocolModule;
import org.trinity.wayland.render.gl.GLModule;

import javax.inject.Singleton;

@Module(
		injects = {
				EntryPoint.class,
				ObjectGraph.class
		},
		includes = {
				//X11Module.class,
				PlatformNewtModule.class,
				GLModule.class,
                WlProtocolModule.class,
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
