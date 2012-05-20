package org.hydrogen.display.api.event.base;

import org.hydrogen.display.api.event.ConfigureNotifyEvent;
import org.hydrogen.display.api.event.DisplayEventSource;
import org.hydrogen.display.api.event.DisplayEventType;

import com.google.inject.Inject;

public class BaseConfigureNotifyEvent extends BaseDisplayEvent implements
		ConfigureNotifyEvent {

	private final int x;
	private final int y;
	private final int width;
	private final int height;

	@Inject
	protected BaseConfigureNotifyEvent(final DisplayEventSource eventSource,
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
