/*
 * This file is part of Fusion-X11.
 * 
 * Fusion-X11 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Fusion-X11 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-X11. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.x11.core;

import java.util.ArrayList;
import java.util.List;

import org.hydrogen.api.display.PlatformRenderArea;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XPropertyXAtomAtoms extends
		XPropertyXAtom<XPropertyInstanceXAtoms> {

	/**
	 * 
	 * @param display
	 * @param atomName
	 * 
	 */
	public XPropertyXAtomAtoms(final XDisplay display, final String atomName) {
		super(display, atomName);
	}

	/**
	 * 
	 * @param display
	 * @param atomName
	 * @param atomId
	 */
	public XPropertyXAtomAtoms(final XDisplay display, final String atomName,
			final Long atomId) {
		super(display, atomName, atomId);
	}

	@Override
	public void setPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceXAtoms propertyInstance) {
		final XAtom[] atoms = propertyInstance.getAtoms();
		final int nroAtoms = atoms.length;
		final IntDataContainer rawDataContainer = new IntDataContainer(nroAtoms);

		for (final XAtom atom : atoms) {
			final Long atomId = atom.getAtomId();
			rawDataContainer.writeDataBlock(atomId.intValue());
		}

		setRawPropertyValue(platformRenderArea, propertyInstance.getType(),
				rawDataContainer);
	}

	@Override
	public XPropertyInstanceXAtoms getPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceInfo propertyInstanceInfo,
			final FlexDataContainer propertyDataContainer) {
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
