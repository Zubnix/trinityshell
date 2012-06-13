/*
 * This file is part of Fusion-X11. Fusion-X11 is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Fusion-X11 is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Fusion-X11. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.display.x11.impl.property;

import java.util.ArrayList;
import java.util.List;

import org.fusion.x11.core.FlexDataContainer;
import org.fusion.x11.core.IntDataContainer;
import org.fusion.x11.core.XAtomRegistry;
import org.trinity.display.x11.api.XAtomFactory;
import org.trinity.display.x11.api.XCall;
import org.trinity.display.x11.api.XCaller;
import org.trinity.display.x11.api.XConnection;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;
import org.trinity.foundation.display.api.PlatformRenderArea;
import org.trinity.foundation.display.api.property.Atom;
import org.trinity.foundation.display.api.property.PropertyInstance;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

// TODO documentation
/**
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XPropertyXAtomAtoms extends AbstractXProperty<Atom[]> {

	/*****************************************
	 * @param xConnection
	 * @param xCaller
	 * @param getPropertyValue
	 * @param setPropertyValue
	 * @param xAtomFactory
	 * @param atomName
	 * @param atomId
	 ****************************************/
	@Inject
	public XPropertyXAtomAtoms(	final XConnection<Long> xConnection,
								final XCaller xCaller,
								final XCall<NativeBufferHelper, Long, Integer> getPropertyValue,
								final XCall<Void, Long, Object> setPropertyValue,
								final XAtomFactory xAtomFactory,
								@Assisted final String atomName,
								@Assisted final int atomId) {
		super(	xConnection,
				xCaller,
				getPropertyValue,
				setPropertyValue,
				xAtomFactory,
				atomName,
				atomId);
	}

	@Override
	public void setPropertyInstance(final PlatformRenderArea platformRenderArea,
									final PropertyInstance<Atom[]> propertyInstance) {
		final XAtom[] atoms = propertyInstance.getAtoms();
		final int nroAtoms = atoms.length;
		final IntDataContainer rawDataContainer = new IntDataContainer(nroAtoms);

		for (final XAtom atom : atoms) {
			final Long atomId = atom.getAtomId();
			rawDataContainer.writeDataBlock(atomId.intValue());
		}

		setRawPropertyValue(platformRenderArea,
							propertyInstance.getType(),
							rawDataContainer);
	}

	@Override
	public PropertyInstance<Atom[]> getPropertyInstance(final PlatformRenderArea platformRenderArea,
														final XPropertyInstanceInfo propertyInstanceInfo,
														final FlexByteContainer propertyDataContainer) {
		final long nroAtomIds = (int) getPrimitivePropertyLength(propertyInstanceInfo);

		final List<XAtom> atoms = new ArrayList<XAtom>((int) nroAtomIds);

		final XAtomRegistry xAtomRegistry = getDisplay().getDisplayAtoms();
		for (int i = 0; i < nroAtomIds; i++) {
			final long atomId = propertyDataContainer.readUnsignedInt();
			final XAtom xAtom = xAtomRegistry.getById(Long.valueOf(atomId));
			if (xAtom != null) {
				atoms.add(xAtom);
			}
		}

		final XAtom[] xAtoms = atoms.toArray(new XAtom[atoms.size()]);

		return new XPropertyInstanceXAtoms(getDisplay(), xAtoms);
	}
}
