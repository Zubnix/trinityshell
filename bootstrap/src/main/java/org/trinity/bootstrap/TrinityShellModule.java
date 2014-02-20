package org.trinity.bootstrap;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;

import javax.inject.Singleton;

@Module(
		injects = {
				EntryPoint.class,
				ObjectGraph.class
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
