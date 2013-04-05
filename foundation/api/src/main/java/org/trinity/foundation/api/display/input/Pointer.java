package org.trinity.foundation.api.display.input;

import org.trinity.foundation.api.display.DisplayArea;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.ButtonNotify;
import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.foundation.api.shared.OwnerThread;

import com.google.common.util.concurrent.ListenableFuture;

@OwnerThread("Display")
public interface Pointer extends InputDevice {
	/***************************************
	 * The position of the pointer as seen from this <code>DisplaySurface</code>
	 * 's coordinate system.
	 * 
	 * @return The pointer position {@link Coordinate}.
	 *************************************** 
	 */
	// TODO pointer notify
	ListenableFuture<Coordinate> getPointerCoordinate(DisplaySurface displaySurface);

	/***************************************
	 * Grab a {@link Button} of the bound {@link DisplayArea} or any of its
	 * children. Grabbing a {@link Button} will override the delivery of the
	 * corresponding {@link ButtonNotify} event so it is only delivered to the
	 * grabber instead of delivering it anyone that is interested.
	 * <p>
	 * This method is usually used to install mousebindings.
	 * 
	 * @param grabButton
	 *            The {@link Button} that should be grabbed.
	 * @param withModifiers
	 *            The {@link InputModifiers} that should be active if a grab is
	 *            to take place.
	 *************************************** 
	 */
	// TODO grab button notify
	ListenableFuture<Void> grabButton(	DisplaySurface displaySurface,
										Button grabButton,
										InputModifiers withModifiers);

	/***************************************
	 * Grab the entire pointing device of the bound {@link DisplayArea} or any
	 * of its children. Every {@link ButtonNotify} shall be redirected.
	 * 
	 * @see #grabButton(Button, InputModifiers)
	 *************************************** 
	 */
	// TODO grab pointer notify
	ListenableFuture<Void> grabPointer(DisplaySurface displaySurface);

	/***************************************
	 * Release the grab on the pointing device.
	 * 
	 * @see #grabPointer()
	 *************************************** 
	 */
	// TODO ungrab pointer notify
	ListenableFuture<Void> ungrabPointer();

	/***************************************
	 * Release the grab on the specific {@link Button} with the specific
	 * {@link InputModifiers}.
	 * 
	 * @param ungrabButton
	 *            The {@link Button} that will be ungrabbed.
	 * @param withModifiers
	 *            The {@link InputModifiers}.
	 * @see #grabButton(Button, InputModifiers)
	 *************************************** 
	 */
	// TODO ungrab button notify
	ListenableFuture<Void> ungrabButton(DisplaySurface displaySurface,
										Button ungrabButton,
										InputModifiers withModifiers);
}
