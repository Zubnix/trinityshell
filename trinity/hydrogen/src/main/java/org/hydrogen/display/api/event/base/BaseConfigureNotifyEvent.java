package org.hydrogen.display.api.event.base;

import org.hydrogen.display.api.event.ConfigureNotifyEvent;
import org.hydrogen.display.api.event.DisplayEventSource;
import org.hydrogen.display.api.event.DisplayEventType;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

public class BaseConfigureNotifyEvent extends BaseDisplayEvent implements
		ConfigureNotifyEvent {

	private final int x;
	private final int y;
	private final int width;
	private final int height;

	@Inject
	protected BaseConfigureNotifyEvent(	@Named("ConfigureNotify") final DisplayEventType configureNotify,
										@Assisted final DisplayEventSource eventSource,
										@Assisted final int x,
										@Assisted final int y,
										@Assisted final int width,
										@Assisted final int height) {
		super(configureNotify, eventSource);
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
