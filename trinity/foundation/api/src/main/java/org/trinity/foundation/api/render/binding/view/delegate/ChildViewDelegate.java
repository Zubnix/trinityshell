package org.trinity.foundation.api.render.binding.view.delegate;

public interface ChildViewDelegate {
	<T> T newView(	Object parentView,
					Class<T> childView,
					int position);

	void destroyView(	Object parentView,
						Object deletedChildView,
						int deletedPosition);

	void updateChildViewPosition(	Object parentView,
									Object childView,
									int oldPosition,
									int newPosition);
}
