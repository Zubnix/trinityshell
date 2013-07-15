package org.trinity.shellplugin.wm.x11.impl.scene;

import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.trinity.foundation.api.render.binding.model.PropertyChanged;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shellplugin.wm.api.HasText;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Bind
@Singleton
@To(IMPLEMENTATION)
@ExecutionContext(ShellExecutor.class)
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

	@Override
	public String getText() {
		return this.text;
	}

	@PropertyChanged("text")
	public void setText(final String text) {
		this.text = text;
	}

	@Override
	public void run() {
		setText(this.sdf.format(new Date()));
	}
}
