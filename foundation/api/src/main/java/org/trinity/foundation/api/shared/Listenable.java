package org.trinity.foundation.api.shared;

import com.google.common.util.concurrent.ListenableFuture;

public interface Listenable {
	ListenableFuture<Void> addListener(Object listener);

	ListenableFuture<Void> removeListener(Object listener);

	ListenableFuture<Void> post(Object event);
}
