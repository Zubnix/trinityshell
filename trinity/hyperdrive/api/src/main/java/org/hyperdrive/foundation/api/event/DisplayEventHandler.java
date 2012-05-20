/*
 * This file is part of HyperDrive.
 *import org.hydrogen.api.display.event.DisplayEvent;
import org.hydrogen.api.display.event.DisplayEventType;
import org.hydrogen.api.event.TypeBoundEventHandler;

 * Foundation, either version 3 of the License, or (at your option) any later
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

import org.hydrogen.display.api.event.DisplayEvent;
import org.hydrogen.display.api.event.DisplayEventType;
import org.hydrogen.event.api.TypeBoundEventHandler;

public interface DisplayEventHandler<E extends DisplayEvent> extends
		TypeBoundEventHandler<DisplayEventType, E> {
}
