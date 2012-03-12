package org.hydrogen.display.event;

import org.hydrogen.api.display.event.ConfigureNotifyEvent;
import org.hydrogen.api.display.event.DisplayEventSource;
import org.hydrogen.api.display.event.DisplayEventType;

public class BaseConfigureNotifyEvent extends BaseDisplayEvent implements
		ConfigureNotifyEvent {

	private final int x;
	private final int y;
	private final int width;
	private final int height;

	public BaseConfigureNotifyEvent(final DisplayEventSource eventSource,
			final int x, final int y, final int width, final int height) {
		super(DisplayEventType.CONFIGURE_NOTIFY, eventSource);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

}
