/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.api.render.binding.view.delegate;

import org.trinity.foundation.api.shared.OwnerThread;

import com.google.common.util.concurrent.ListenableFuture;

/***************************************
 * A delegate to handle the life cycle of a child view element. This delegate
 * should be implemented for a specific widget toolkit.
 * 
 * 
 *************************************** 
 */
@OwnerThread("Render")
public interface ChildViewDelegate {
	/***************************************
	 * Create a new view instance.
	 * 
	 * @param parentView
	 *            The parent view of the new view instance.
	 * @param childViewType
	 *            The type of the new view instance.
	 * @param position
	 *            The position (index) of the new view, relative to it's parent.
	 * @return a new view instance.
	 *************************************** 
	 */
	<T> ListenableFuture<T> newView(Object parentView,
									Class<T> childViewType,
									int position);

	/***************************************
	 * Destroy a view instance.
	 * 
	 * @param parentView
	 *            The parent of the view that should be destroyed.
	 * @param deletedChildView
	 *            The view that should be destroyed.
	 * @param deletedPosition
	 *            The index of the view that should be destroyed.
	 *************************************** 
	 */
	// TODO some kind of notify event?
	ListenableFuture<Void> destroyView(	Object parentView,
										Object deletedChildView,
										int deletedPosition);

	/***************************************
	 * Update the position (index) of a view instance, relative to it's parent.
	 * 
	 * @param parentView
	 *            The parent view.
	 * @param childView
	 *            The view who's position should be updated.
	 * @param oldPosition
	 *            The old position.
	 * @param newPosition
	 *            The new position.
	 *************************************** 
	 */
	// TODO some kind of notify event?
	ListenableFuture<Void> updateChildViewPosition(	Object parentView,
													Object childView,
													int oldPosition,
													int newPosition);
}
