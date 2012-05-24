/*
 * This file is part of Fusion-X11.
 * 
 *import org.fusion.x11.core.XID;
import org.fusion.x11.core.XResource;
import org.fusion.x11.core.extension.XDamage;
License as published by the Free Software
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
package org.fusion.x11.core.xcb.extension;

import org.fusion.x11.core.XID;
import org.fusion.x11.core.XResource;
import org.fusion.x11.core.extension.XDamage;

// currently unused
public final class XcbXDamage extends XResource implements XDamage {
	XcbXDamage(final XID xid) {
		super(xid);
	}
}
