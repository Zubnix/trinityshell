/*
 * This file is part of Fusimport org.fusion.x11.core.FlexDataContainer; import
 * org.fusion.x11.core.IntDataContainer; import org.fusion.x11.core.XDisplay;
 * import org.fusion.x11.core.XPropertyInstanceInfo; import
 * org.fusion.x11.core.XPropertyXAtom; import
 * org.hydrogen.displayinterface.PlatformRenderArea; import
 * org.hydrogen.error.HydrogenDisplayInterfaceException; HOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-X11. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.x11.ewmh;

import org.fusion.x11.core.FlexDataContainer;
import org.fusion.x11.core.IntDataContainer;
import org.fusion.x11.core.XDisplay;
import org.fusion.x11.core.XPropertyInstanceInfo;
import org.fusion.x11.core.XPropertyXAtom;
import org.hydrogen.api.display.PlatformRenderArea;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class _NetWmHandledIcons extends
		XPropertyXAtom<_NetWmHandledIconsInstance> {

	public static final String ATOM_NAME = "_NET_WM_HANDLED_ICONS";

	/**
	 * 
	 * @param display
	 * 
	 */
	public _NetWmHandledIcons(final XDisplay display) {
		super(display, _NetWmHandledIcons.ATOM_NAME);
	}

	@Override
	public void setPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final _NetWmHandledIconsInstance propertyInstance) {
		// TODO
		final IntDataContainer rawDataContainer = new IntDataContainer(
				new byte[] {});
		setRawPropertyValue(platformRenderArea, propertyInstance.getType(),
				rawDataContainer);
	}

	@Override
	public _NetWmHandledIconsInstance getPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceInfo propertyInstanceInfo,
			final FlexDataContainer propertyDataContainer) {
		final _NetWmHandledIconsInstance reply = new _NetWmHandledIconsInstance(
				getDisplay());
		return reply;
	}
}
