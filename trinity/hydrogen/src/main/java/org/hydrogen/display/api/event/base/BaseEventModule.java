package org.hydrogen.display.api.event.base;

import org.hydrogen.display.api.event.ButtonNotifyEvent;
import org.hydrogen.display.api.event.ClientMessageEvent;
import org.hydrogen.display.api.event.ConfigureNotifyEvent;
import org.hydrogen.display.api.event.ConfigureRequestEvent;
import org.hydrogen.display.api.event.DestroyNotifyEvent;
import org.hydrogen.display.api.event.DisplayEvent;
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

public class BaseEventModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DisplayEvent.class).to(BaseDisplayEvent.class);
		bind(InputNotifyEvent.class).to(BaseInputNotifyEvent.class);
		bind(ButtonNotifyEvent.class).to(BaseButtonNotifyEvent.class);
		bind(ClientMessageEvent.class).to(BaseClientMessageEvent.class);
		bind(ConfigureNotifyEvent.class).to(BaseConfigureNotifyEvent.class);
		bind(ConfigureRequestEvent.class).to(BaseConfigureRequestEvent.class);
		bind(DestroyNotifyEvent.class).to(BaseDestroyNotifyEvent.class);
		bind(FocusNotifyEvent.class).to(BaseFocusInNotifyEvent.class);
		bind(FocusNotifyEvent.class).to(BaseFocusOutNotifyEvent.class);
		bind(KeyNotifyEvent.class).to(BaseKeyNotifyEvent.class);
		bind(MapNotifyEvent.class).to(BaseMapNotifyEvent.class);
		bind(MapRequestEvent.class).to(BaseMapRequestEvent.class);
		bind(MouseEnterLeaveNotifyEvent.class).to(
				BaseMouseEnterNotifyEvent.class);
		bind(MouseEnterLeaveNotifyEvent.class).to(
				BaseMouseLeaveNotifyEvent.class);
		bind(PropertyChangedNotifyEvent.class).to(
				BasePropertyChangedNotifyEvent.class);
		bind(StackingNotifyEvent.class).to(BaseStackingNotifyEvent.class);
		bind(UnmappedNotifyEvent.class).to(BaseUnmappedNotifyEvent.class);
	}
}