package org.trinity.x11.defaul.xeventhandler;

import dagger.Module;
import dagger.Provides;
import org.trinity.x11.defaul.XEventHandler;

import static dagger.Provides.Type.SET;

@Module(
		injects = {
				CirculateNotifyHandler.class,
				ClientMessageHandler.class,
				ConfigureNotifyHandler.class,
				ConfigureRequestHandler.class,
				DestroyNotifyHandler.class,
				EnterNotifyHandler.class,
				FocusInHandler.class,
				FocusOutHandler.class,
				GenericErrorHandler.class,
				LeaveNotifyHandler.class,
				MapNotifyHandler.class,
				MapRequestHandler.class,
				UnmapNotifyHandler.class,
		},
		complete = false,
		library = true
)
public class XEventHandlersModule {

	@Provides(type = SET)
	XEventHandler provideXEventHandler(final CirculateNotifyHandler circulateNotifyHandler) {
		return circulateNotifyHandler;
	}

	@Provides(type = SET)
	XEventHandler provideXEventHandler(final ClientMessageHandler clientMessageHandler){
		return clientMessageHandler;
	}

	@Provides(type = SET)
	XEventHandler provideXEventHandler(final ConfigureNotifyHandler configureNotifyHandler) {
		return configureNotifyHandler;
	}

	@Provides(type = SET)
	XEventHandler provideXEventHandler(final ConfigureRequestHandler configureRequestHandler){
		return configureRequestHandler;
	}

	@Provides(type = SET)
	XEventHandler provideXEventHandler(final DestroyNotifyHandler destroyNotifyHandler){
		return destroyNotifyHandler;
	}

	@Provides(type = SET)
	XEventHandler provideXEventHandler(final EnterNotifyHandler enterNotifyHandler){
		return enterNotifyHandler;
	}

	@Provides(type = SET)
	XEventHandler provideXEventHandler(final FocusInHandler focusInHandler){
		return focusInHandler;
	}

	@Provides(type = SET)
	XEventHandler provideXEventHandler(final FocusOutHandler focusOutHandler){
		return focusOutHandler;
	}

	@Provides(type = SET)
	XEventHandler provideXEventHandler(final GenericErrorHandler genericErrorHandler){
		return genericErrorHandler;
	}

	@Provides(type = SET)
	XEventHandler provideXEventHandler(final LeaveNotifyHandler leaveNotifyHandler){
		return leaveNotifyHandler;
	}

	@Provides(type = SET)
	XEventHandler provideXEventHandler(final MapNotifyHandler mapNotifyHandler){
		return mapNotifyHandler;
	}

	@Provides(type = SET)
	XEventHandler provideXEventHandler(final MapRequestHandler mapRequestHandler){
		return mapRequestHandler;
	}

	@Provides(type = SET)
	XEventHandler provideXEventHandler(final UnmapNotifyHandler unmapNotifyHandler){
		return unmapNotifyHandler;
	}
}
