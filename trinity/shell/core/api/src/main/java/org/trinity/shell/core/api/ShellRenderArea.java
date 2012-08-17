/*
 * This file is part of HyperDriimport
 * org.hydrogen.api.display.PlatformRenderArea; import
 * org.hydrogen.api.display.Property; import
 * org.hydrogen.api.display.PropertyInstance; import
 * org.hyperdrive.api.core.event.PropertyChangedHandler; import
 * org.hyperdrive.api.geo.GeoTransformableRectangle; HyperDrive is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with HyperDrive. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.core.api;

import org.trinity.foundation.display.api.DisplayRenderArea;
import org.trinity.shell.geo.api.ShellGeoNode;

public interface ShellRenderArea extends ShellGeoNode {
	int getHeightIncrement();

	int getMaxHeight();

	int getMaxWidth();

	int getMinHeight();

	int getMinWidth();

	DisplayRenderArea getDisplayRenderArea();

	int getWidthIncrement();

	boolean isMovable();

	boolean isResizable();

	void setHeightIncrement(final int heightIncrement);

	void setMaxHeight(final int maxHeight);

	void setMaxWidth(final int maxWidth);

	void setMinHeight(final int minHeight);

	void setMinWidth(final int minWidth);

	void setMovable(final boolean movable);

	void setResizable(final boolean isResizable);

	void setWidthIncrement(final int widthIncrement);

	void syncGeoToDisplayRenderArea();

	void setInputFocus();
}
