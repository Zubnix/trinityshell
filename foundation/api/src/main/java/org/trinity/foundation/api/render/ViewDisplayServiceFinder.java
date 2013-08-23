package org.trinity.foundation.api.render;

import org.trinity.foundation.api.display.DisplaySurface;

import com.google.common.util.concurrent.ListenableFuture;

/**
 *
 */
public interface ViewDisplayServiceFinder {
	ListenableFuture<DisplaySurface> find(Object view);
}
