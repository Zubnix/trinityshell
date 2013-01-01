package org.trinity.foundation.api.render.binding;

import java.lang.reflect.Method;

import ca.odell.glazedlists.EventList;

public interface ObservedCollectionHandler {
	void handleObservedCollection(	Object dataContext,
									Object view,
									ObservedCollection observedCollectionMeta,
									Method observedCollection,
									EventList<?> collection);
}
