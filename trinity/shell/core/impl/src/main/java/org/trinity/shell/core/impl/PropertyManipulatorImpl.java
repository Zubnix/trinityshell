package org.trinity.shell.core.impl;
// /*
// * This file is part of HyperDrive. HyperDrive is free software: you can
// * redistribute it and/or modify it under the terms of the GNU General Public
// * License as published by the Free Software Foundation, either version 3 of
// the
// * License, or (at your option) any later version. HyperDrive is distributed
// in
// * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
// * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
// See
// * the GNU General Public License for more details. You should have received a
// * copy of the GNU General Public License along with HyperDrive. If not, see
// * <http://www.gnu.org/licenses/>.
// */
// package org.trinity.shell.foundation.impl;
//
// import org.apache.log4j.Logger;
// import org.trinity.foundation.display.api.property.Property;
// import org.trinity.foundation.display.api.property.PropertyInstance;
// import org.trinity.shell.foundation.api.PropertyManipulator;
// import org.trinity.shell.foundation.api.RenderArea;
//
// import com.google.inject.Inject;
// import com.google.inject.assistedinject.Assisted;
//
// // TODO documentation
// /**
// * @author Erik De Rijcke
// * @since 1.0
// */
// public class PropertyManipulatorImpl implements PropertyManipulator {
//
// private static final Logger LOGGER = Logger
// .getLogger(PropertyManipulatorImpl.class);
// private static final String PROPERTY_NOT_FOUND_ERROR_MSG =
// "No property with name %s is known.";
//
// private final RenderArea renderArea;
//
// /**
// * @param renderArea
// */
// @Inject
// protected PropertyManipulatorImpl(@Assisted final RenderArea renderArea) {
// // protected to reduce visibility and encourage dependency injection
// this.renderArea = renderArea;
// }
//
// /**
// * @return
// */
// @Override
// public RenderArea getRenderArea() {
// return this.renderArea;
// }
//
// /**
// * @param propertyName
// * @return An instance of the given property name, null if the property is
// * not known.
// */
// @Override
// public <T extends PropertyInstance> T getPropertyValue(final String
// propertyName) {
// // TODO from factory
// @SuppressWarnings("unchecked")
// final Property<T> property = (Property<T>) getRenderArea()
// .getManagedDisplay().getDisplay().getDisplayAtoms()
// .getAtomByName(propertyName);
// if (property != null) {
// final T propertyInstance = getRenderArea().getPlatformRenderArea()
// .getPropertyInstance(property);
// return propertyInstance;
// } else {
// PropertyManipulatorImpl.LOGGER
// .error(String
// .format(PropertyManipulatorImpl.PROPERTY_NOT_FOUND_ERROR_MSG,
// propertyName));
// return null;
// }
// }
//
// /**
// * @param propertyName
// * @param propertyInstance
// */
// @Override
// public <T extends PropertyInstance> void setPropertyValue( final String
// propertyName,
// final T propertyInstance) {
//
// @SuppressWarnings("unchecked")
// final Property<T> property = (Property<T>) getRenderArea()
// .getManagedDisplay().getDisplay().getDisplayAtoms()
// .getAtomByName(propertyName);
//
// getRenderArea().getPlatformRenderArea()
// .setPropertyInstance(property, propertyInstance);
// }
// }
