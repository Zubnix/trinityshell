package org.trinity.render.swt.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.trinity.foundation.display.api.DisplaySurfaceHandle;

public class SwtDisplaySurfaceHandle implements DisplaySurfaceHandle {

	private final Composite visual;

	public SwtDisplaySurfaceHandle(Composite visual) {
		this.visual = visual;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof DisplaySurfaceHandle) {
			final DisplaySurfaceHandle otherObj = (DisplaySurfaceHandle) obj;
			return otherObj.getNativeHandle().equals(getNativeHandle());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getNativeHandle().hashCode();
	}

	@Override
	public Object getNativeHandle() {
		final FutureTask<Integer> getHandleTask = new FutureTask<Integer>(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				return Integer.valueOf(visual.handle);
			}
		});
		Display.getDefault().asyncExec(getHandleTask);
		Integer handle = null;
		try {
			handle = getHandleTask.get();
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return handle;
	}
}