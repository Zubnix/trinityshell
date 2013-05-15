package org.trinity.shellplugin.wm.x11.impl.scene;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.trinity.foundation.api.render.binding.model.PropertyChanged;
import org.trinity.shellplugin.wm.api.HasText;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(value = Type.IMPLEMENTATION))
@Singleton
public class Clock implements HasText, Runnable {

	private final ScheduledExecutorService clockExecutor = Executors.newScheduledThreadPool(1);
	private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

	private String text;

	@Inject
	Clock(final ShellRootWidget shellRootWidget) {
		shellRootWidget.getNotificationsBar().add(this);
		this.clockExecutor.scheduleAtFixedRate(	this,
												0,
												1,
												TimeUnit.MINUTES);
	}

	@PropertyChanged("text")
	public void setText(final String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return this.text;
	}

	@Override
	public void run() {
		setText(this.sdf.format(new Date()));
	}
}