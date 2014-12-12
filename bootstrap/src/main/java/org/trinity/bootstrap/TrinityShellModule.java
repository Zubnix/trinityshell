package org.trinity.bootstrap;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import org.trinity.platform.newt.PlatformNewtModule;
import org.trinity.wayland.input.newt.InputNewtModule;
import org.trinity.wayland.output.gl.OutputGLModule;
import org.trinity.wayland.protocol.ProtocolModule;

import javax.inject.Singleton;

@Module(
		injects = {
				EntryPoint.class,
				ObjectGraph.class
		},
		includes = {
				//X11Module.class,
				PlatformNewtModule.class,
				InputNewtModule.class,
				OutputGLModule.class,
                ProtocolModule.class,
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
