package org.trinity.shellplugin.wm.view.javafx.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.inject.Provider;
import org.trinity.foundation.api.render.ViewReference;

import java.util.concurrent.Callable;

public class DesktopViewReferenceProvider implements Provider<ListenableFuture<ViewReference>>{
	@Override
	public ListenableFuture<ViewReference> get() {

		ListenableFutureTask<ViewReference> task = ListenableFutureTask.create(new Callable<ViewReference>() {
			@Override
			public ViewReference call() throws Exception {
				return null;
			}
		});
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}
}
