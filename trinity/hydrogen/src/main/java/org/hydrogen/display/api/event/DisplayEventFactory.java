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
package org.hydrogen.display.api.event;

import org.hydrogen.display.api.Atom;
import org.hydrogen.display.api.Property;
import org.hydrogen.display.api.PropertyInstance;
import org.hydrogen.display.api.input.KeyboardInput;
import org.hydrogen.display.api.input.MouseInput;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public interface DisplayEventFactory {

	@Named("ButtonPressedEvent")
	ButtonNotifyEvent createButtonPressed(	DisplayEventSource eventSource,
											MouseInput input);

	@Named("ButtonReleasedEvent")
	ButtonNotifyEvent createButtonReleased(	DisplayEventSource eventSource,
											MouseInput input);

	ClientMessageEvent createClientMessage(	DisplayEventSource platformRenderAreaArgument,
											final Atom dataType,
											int dataFormat,
											byte[] data);

	ConfigureNotifyEvent createConfigureNotify(	DisplayEventSource eventSource,
												@Assisted("x") int x,
												@Assisted("y") int y,
												@Assisted("width") int width,
												@Assisted("height") int height);

	ConfigureRequestEvent createConfigureRequest(	DisplayEventSource eventSource,
													@Assisted("xSet") boolean xSet,
													@Assisted("ySet") boolean ySet,
													@Assisted("heightSet") boolean heightSet,
													@Assisted("widthSet") boolean widthSet,
													@Assisted("x") int x,
													@Assisted("y") int y,
													@Assisted("width") int width,
													@Assisted("height") int height);

	DestroyNotifyEvent createDestroyNotify(DisplayEventSource destroyedEventSource);

	@Named("FocusGainNotifyEvent")
	FocusNotifyEvent createFocusGained(DisplayEventSource eventSource);

	@Named("FocusLostNotifyEvent")
	FocusNotifyEvent createFocusLost(DisplayEventSource eventSource);

	@Named("KeyPressedEvent")
	KeyNotifyEvent createKeyPressed(DisplayEventSource eventSource,
									KeyboardInput input);

	@Named("KeyReleasedEvent")
	KeyNotifyEvent createKeyReleased(	DisplayEventSource eventSource,
										KeyboardInput input);

	MapNotifyEvent createMapNotify(DisplayEventSource eventSource);

	MapRequestEvent createMapRequest(DisplayEventSource eventSource);

	@Named("MouseEnterEvent")
	MouseVisitationNotifyEvent createMouseEnter(DisplayEventSource eventSource);

	@Named("MouseLeaveEvent")
	MouseVisitationNotifyEvent createMouseLeave(DisplayEventSource eventSource);

	PropertyChangedNotifyEvent createPropertyChangedNotify(	DisplayEventSource eventSource,
															boolean propertyDeleted,
															Property<? extends PropertyInstance> changedProperty);

	StackingChangedNotifyEvent createStackingChangedNotify(DisplayEventSource eventSource);

	UnmappedNotifyEvent createUnmappedNotify(DisplayEventSource eventSource);

}
