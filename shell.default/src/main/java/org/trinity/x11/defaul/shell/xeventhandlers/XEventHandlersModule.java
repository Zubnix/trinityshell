package org.trinity.x11.defaul.shell.xeventhandlers;

import dagger.Module;
import dagger.Provides;
import org.trinity.x11.defaul.XEventHandler;

import static dagger.Provides.Type.SET;

@Module(
		injects = {
				ConfigureRequest.class,
				DestroyNotify.class,
				GenericError.class,
				MapRequest.class,
				UnmapNotify.class,
                ClientMessage.class,
		},
		complete = false,
		library = true
)
public class XEventHandlersModule {
	@Provides(type = SET)
	XEventHandler provideXEventHandler(final ConfigureRequest configureRequest){ return configureRequest; }
	@Provides(type = SET)
	XEventHandler provideXEventHandler(final DestroyNotify destroyNotify){ return destroyNotify; }
	@Provides(type = SET)
	XEventHandler provideXEventHandler(final GenericError genericError){ return genericError; }
	@Provides(type = SET)
	XEventHandler provideXEventHandler(final MapRequest mapRequest){ return mapRequest; }
	@Provides(type = SET)
	XEventHandler provideXEventHandler(final UnmapNotify unmapNotify){ return unmapNotify; }
    @Provides
    XEventHandler provideXEventHandler(final ClientMessage clientMessage) { return clientMessage; }
}
