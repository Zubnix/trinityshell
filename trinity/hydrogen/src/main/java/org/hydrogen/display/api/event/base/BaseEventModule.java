package org.hydrogen.display.api.event.base;

import org.hydrogen.display.api.event.ButtonNotifyEvent;
import org.hydrogen.display.api.event.ClientMessageEvent;
import org.hydrogen.display.api.event.ConfigureNotifyEvent;
import org.hydrogen.display.api.event.ConfigureRequestEvent;
import org.hydrogen.display.api.event.DestroyNotifyEvent;
import org.hydrogen.display.api.event.DisplayEvent;
import org.hydrogen.display.api.event.DisplayEventType;
import org.hydrogen.display.api.event.FocusNotifyEvent;
import org.hydrogen.display.api.event.InputNotifyEvent;
import org.hydrogen.display.api.event.KeyNotifyEvent;
import org.hydrogen.display.api.event.MapNotifyEvent;
import org.hydrogen.display.api.event.MapRequestEvent;
import org.hydrogen.display.api.event.MouseEnterLeaveNotifyEvent;
import org.hydrogen.display.api.event.PropertyChangedNotifyEvent;
import org.hydrogen.display.api.event.StackingNotifyEvent;
import org.hydrogen.display.api.event.UnmappedNotifyEvent;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class BaseEventModule extends AbstractModule {

	@Override
	protected void configure() {
		// start bindings of event type instances
		// TODO instead of instance use eager singleton. How to access singleton
		// instance?
		bind(DisplayEventType.class)
				.annotatedWith(Names.named("ButtonPressed"))
				.toInstance(DisplayEventType.BUTTON_PRESSED);
		bind(DisplayEventType.class)
				.annotatedWith(Names.named("ButtonReleased"))
				.toInstance(DisplayEventType.BUTTON_RELEASED);
		bind(DisplayEventType.class)
				.annotatedWith(Names.named("ClientMessage"))
				.toInstance(DisplayEventType.CLIENT_MESSAGE);
		bind(DisplayEventType.class)
				.annotatedWith(Names.named("ConfigureNotify"))
				.toInstance(DisplayEventType.CONFIGURE_NOTIFY);
		bind(DisplayEventType.class)
				.annotatedWith(Names.named("ConfigureRequest"))
				.toInstance(DisplayEventType.CONFIGURE_REQUEST);
		bind(DisplayEventType.class)
				.annotatedWith(Names.named("DestroyNotify"))
				.toInstance(DisplayEventType.DESTROY_NOTIFY);
		bind(DisplayEventType.class)
				.annotatedWith(Names.named("FocusGainNotify"))
				.toInstance(DisplayEventType.FOCUS_GAIN_NOTIFY);
		bind(DisplayEventType.class)
				.annotatedWith(Names.named("FocusLostNotify"))
				.toInstance(DisplayEventType.FOCUS_LOST_NOTIFY);
		bind(DisplayEventType.class).annotatedWith(Names.named("KeyPressed"))
				.toInstance(DisplayEventType.KEY_PRESSED);
		bind(DisplayEventType.class).annotatedWith(Names.named("KeyReleased"))
				.toInstance(DisplayEventType.KEY_RELEASED);

		// end bindings of even type instances

		// start bindings of events
		bind(DisplayEvent.class).to(BaseDisplayEvent.class);
		bind(InputNotifyEvent.class).to(BaseInputNotifyEvent.class);

		bind(ButtonNotifyEvent.class).to(BaseButtonNotifyEvent.class);
		bind(BaseButtonNotifyEvent.class)
				.annotatedWith(Names.named("ButtonPressedEvent"))
				.to(BaseButtonPressedNotifyEvent.class);
		bind(BaseButtonNotifyEvent.class)
				.annotatedWith(Names.named("ButtonReleasedEvent"))
				.to(BaseButtonReleasedNotifyEvent.class);

		bind(ClientMessageEvent.class).to(BaseClientMessageEvent.class);
		bind(ConfigureNotifyEvent.class).to(BaseConfigureNotifyEvent.class);
		bind(ConfigureRequestEvent.class).to(BaseConfigureRequestEvent.class);
		bind(DestroyNotifyEvent.class).to(BaseDestroyNotifyEvent.class);

		bind(FocusNotifyEvent.class)
				.annotatedWith(Names.named("FocusGainNotifyEvent"))
				.to(BaseFocusGainNotifyEvent.class);
		bind(FocusNotifyEvent.class)
				.annotatedWith(Names.named("FocusLostNotifyEvent"))
				.to(BaseFocusLostNotifyEvent.class);

		bind(KeyNotifyEvent.class).to(BaseKeyNotifyEvent.class);
		bind(BaseKeyNotifyEvent.class)
				.annotatedWith(Names.named("KeyPressedEvent"))
				.to(BaseKeyPressedNotifyEvent.class);
		bind(BaseKeyNotifyEvent.class)
				.annotatedWith(Names.named("KeyReleasedEvent"))
				.to(BaseKeyReleasedNotifyEvent.class);

		bind(MapNotifyEvent.class).to(BaseMapNotifyEvent.class);
		bind(MapRequestEvent.class).to(BaseMapRequestEvent.class);
		bind(MouseEnterLeaveNotifyEvent.class)
				.to(BaseMouseEnterNotifyEvent.class);
		bind(MouseEnterLeaveNotifyEvent.class)
				.to(BaseMouseLeaveNotifyEvent.class);
		bind(PropertyChangedNotifyEvent.class)
				.to(BasePropertyChangedNotifyEvent.class);
		bind(StackingNotifyEvent.class).to(BaseStackingNotifyEvent.class);
		bind(UnmappedNotifyEvent.class).to(BaseUnmappedNotifyEvent.class);
		// end bindings of events

		// start bindings of event factories

		// end bindings of event factories
	}
}