/*
 * This file is part of HyperDriimport org.hydrogen.api.display.PlatformRenderArea;
import org.hydrogen.api.display.Property;
import org.hydrogen.api.display.PropertyInstance;
import org.hyperdrive.api.core.event.PropertyChangedHandler;
import org.hyperdrive.api.geo.GeoTransformableRectangle;
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hyperdrive.foundation.api;

import org.hyperdrive.foundation.api.event.PropertyChangedHandler;
import org.hyperdrive.geo.api.GeoTransformableRectangle;
import org.trinity.core.display.api.PlatformRenderArea;
import org.trinity.core.display.api.Property;
import org.trinity.core.display.api.PropertyInstance;

public interface RenderArea extends GeoTransformableRectangle {
	int getHeightIncrement();

	ManagedDisplay getManagedDisplay();

	int getMaxHeight();

	int getMaxWidth();

	int getMinHeight();

	int getMinWidth();

	PlatformRenderArea getPlatformRenderArea();

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

	void syncGeoToPlatformRenderAreaGeo();

	void setInputFocus();

	boolean hasInputFocus();

	void addPropertyChangedHandler(
			PropertyChangedHandler<? extends Property<? extends PropertyInstance>> handler,
			String propertyName);
}
