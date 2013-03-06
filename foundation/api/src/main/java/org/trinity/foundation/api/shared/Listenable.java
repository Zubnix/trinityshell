package org.trinity.foundation.api.shared;

public interface Listenable {
	void addListener(Object listener);

	void removeListener(Object listener);

	void post(Object event);
}
