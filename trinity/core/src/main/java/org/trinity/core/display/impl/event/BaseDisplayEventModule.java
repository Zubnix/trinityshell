package org.trinity.core.display.impl.event;

import org.trinity.core.display.api.event.ButtonNotifyEvent;
import org.trinity.core.display.api.event.ClientMessageEvent;
import org.trinity.core.display.api.event.ConfigureNotifyEvent;
import org.trinity.core.display.api.event.ConfigureRequestEvent;
import org.trinity.core.display.api.event.DestroyNotifyEvent;
import org.trinity.core.display.api.event.DisplayEventFactory;
import org.trinity.core.display.api.event.DisplayEventType;
import org.trinity.core.display.api.event.FocusNotifyEvent;
import org.trinity.core.display.api.event.KeyNotifyEvent;
import org.trinity.core.display.api.event.MapNotifyEvent;
import org.trinity.core.display.api.event.MapRequestEvent;
import org.trinity.core.display.api.event.MouseVisitationNotifyEvent;
import org.trinity.core.display.api.event.PropertyChangedNotifyEvent;
import org.trinity.core.display.api.event.StackingChangedNotifyEvent;
import org.trinity.core.display.api.event.UnmappedNotifyEvent;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

public class BaseDisplayEventModule extends AbstractModule {

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
		bind(DisplayEventType.class).annotatedWith(Names.named("MapNotify"))
				.toInstance(DisplayEventType.MAP_NOTIFY);
		bind(DisplayEventType.class).annotatedWith(Names.named("MapRequest"))
				.toInstance(DisplayEventType.MAP_REQUEST);
		bind(DisplayEventType.class).annotatedWith(Names.named("MouseEnter"))
				.toInstance(DisplayEventType.MOUSE_ENTER);
		bind(DisplayEventType.class).annotatedWith(Names.named("MouseLeave"))
				.toInstance(DisplayEventType.MOUSE_LEAVE);
		bind(DisplayEventType.class)
				.annotatedWith(Names.named("PropertyChanged"))
				.toInstance(DisplayEventType.PROPERTY_CHANGED);
		bind(DisplayEventType.class)
				.annotatedWith(Names.named("StackingChanged"))
				.toInstance(DisplayEventType.STACKING_CHANGED);
		bind(DisplayEventType.class).annotatedWith(Names.named("UnmapNotify"))
				.toInstance(DisplayEventType.UNMAP_NOTIFY);
		// end bindings of even type instances

		// start bindings of event factory
		install(new FactoryModuleBuilder()
				.implement(	ButtonNotifyEvent.class,
							Names.named("ButtonPressedEvent"),
							BaseButtonPressedNotifyEvent.class)
				.implement(	ButtonNotifyEvent.class,
							Names.named("ButtonReleasedEvent"),
							BaseButtonReleasedNotifyEvent.class)
				.implement(	ClientMessageEvent.class,
							BaseClientMessageEvent.class)
				.implement(	ConfigureNotifyEvent.class,
							BaseConfigureNotifyEvent.class)
				.implement(	ConfigureRequestEvent.class,
							BaseConfigureRequestEvent.class)
				.implement(	DestroyNotifyEvent.class,
							BaseDestroyNotifyEvent.class)
				.implement(	FocusNotifyEvent.class,
							Names.named("FocusGainNotifyEvent"),
							BaseFocusGainNotifyEvent.class)
				.implement(	FocusNotifyEvent.class,
							Names.named("FocusLostNotifyEvent"),
							BaseFocusLostNotifyEvent.class)
				.implement(	KeyNotifyEvent.class,
							Names.named("KeyPressedEvent"),
							BaseKeyPressedNotifyEvent.class)
				.implement(	KeyNotifyEvent.class,
							Names.named("KeyReleasedEvent"),
							BaseKeyReleasedNotifyEvent.class)
				.implement(MapNotifyEvent.class, BaseMapNotifyEvent.class)
				.implement(MapRequestEvent.class, BaseMapRequestEvent.class)
				.implement(	MouseVisitationNotifyEvent.class,
							Names.named("MouseEnterEvent"),
							BaseMouseEnterNotifyEvent.class)
				.implement(	MouseVisitationNotifyEvent.class,
							Names.named("MouseLeaveEvent"),
							BaseMouseLeaveNotifyEvent.class)
				.implement(	PropertyChangedNotifyEvent.class,
							BasePropertyChangedNotifyEvent.class)
				.implement(	StackingChangedNotifyEvent.class,
							BaseStackingChangedNotifyEvent.class)
				.implement(	UnmappedNotifyEvent.class,
							BaseUnmappedNotifyEvent.class)
				.build(DisplayEventFactory.class));
		// end bindings of event factory
	}
}