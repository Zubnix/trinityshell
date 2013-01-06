package org.trinity.foundation.api.render.binding.view.delegate;

public interface ChildViewDelegate {
	<T> T newView(	Object parentView,
					Class<T> childView);

	void destroyView(Object childView);
}
