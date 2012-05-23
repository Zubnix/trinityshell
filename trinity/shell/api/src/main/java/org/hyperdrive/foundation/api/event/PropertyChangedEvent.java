/*
 * This file is part of HyperDrive.
 * import org.hydrogen.api.display.Property;
import org.hydrogen.api.display.PropertyInstance;
import org.hydrogen.api.event.Event;
import org.hyperdrive.api.core.RenderArea;
on, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hyperdrive.foundation.api.event;

import org.hyperdrive.foundation.api.RenderArea;
import org.trinity.core.display.api.Property;
import org.trinity.core.display.api.PropertyInstance;
import org.trinity.core.event.api.Event;

public interface PropertyChangedEvent<T extends Property<? extends PropertyInstance>>
		extends Event<PropertyType> {

	PropertyTypes TYPE = new PropertyTypes();

	RenderArea getRenderArea();

	T getProperty();

	boolean isPropertyDeleted();
}
