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

package org.fusion.x11.icccm;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public abstract class WmNormalHintsParser {

	public void parseWmSizeHints(final WmSizeHintsInstance sizeHintsReply) {

		final long normalHintFlags = sizeHintsReply.getFlags();
		int valueMask = 0;
		short x = 0;
		short y = 0;
		int width = 0;
		int height = 0;
		int minWidth = 0;
		int minHeight = 0;
		int maxWidth = 0;
		int maxHeight = 0;
		int widthIncrement = 0;
		int heightIncrement = 0;
		// USPosition 1 User-specified x, y
		// USSize 2 User-specified width, height
		// PPosition 4 Program-specified position
		// PSize 8 Program-specified size
		// PMinSize 16 Program-specified minimum size
		// PMaxSize 32 Program-specified maximum size
		// PResizeInc 64 Program-specified resize increments
		// PAspect 128 Program-specified min and max aspect ratios
		// PBaseSize 256 Program-specified base size
		// PWinGravity 512 Program-specified window gravity
		if ((normalHintFlags & 1) != 0) {
			x = (short) sizeHintsReply.getX();
			y = (short) sizeHintsReply.getY();
			valueMask |= 1;
			valueMask |= 2;
		}
		// if ((normalHintFlags & 2) != 0) {
		// // obsolete
		// // width = sizeHintsReply.getWidth();
		// // height = sizeHintsReply.getHeight();
		// // valueMask |= 4;
		// // valueMask |= 8;
		// }
		if ((normalHintFlags & 4) != 0) {
			if (x == 0) {
				valueMask |= 1;
				x = (short) sizeHintsReply.getX();
			}
			if (y == 0) {
				valueMask |= 2;
				y = (short) sizeHintsReply.getY();
			}
		}
		if ((normalHintFlags & 8) != 0) {
			if (width == 0) {
				width = sizeHintsReply.getWidth();
				valueMask |= 4;
			}
			if (height == 0) {
				height = sizeHintsReply.getHeight();
				valueMask |= 8;
			}
		}
		if ((normalHintFlags & 16) != 0) {
			// EWMH:
			// Windows can indicate that they are non-resizable by setting
			// minheight = maxheight and minwidth = maxwidth in the ICCCM
			// WM_NORMAL_HINTS property. The Window Manager MAY decorate such
			// windows differently.

			minWidth = sizeHintsReply.getMinWidth();
			minHeight = sizeHintsReply.getMinHeight();

		}
		if ((normalHintFlags & 32) != 0) {
			maxWidth = sizeHintsReply.getMaxWidth();
			maxHeight = sizeHintsReply.getMaxHeight();

		}
		if ((normalHintFlags & 64) != 0) {
			widthIncrement = sizeHintsReply.getWidthInc();
			heightIncrement = sizeHintsReply.getHeightInc();
		}
		// if ((normalHintFlags & 128) != 0) {
		// // not interested
		// }
		// if ((normalHintFlags & 256) != 0) {
		// // if (width == 0) {
		// // width = sizeHintsReply.getBaseWidth();
		// // valueMask |= 4;
		// // }
		// // if (height == 0) {
		// // height = sizeHintsReply.getBaseHeight();
		// // valueMask |= 8;
		// // }
		// }
		// if ((normalHintFlags & 512) != 0) {
		// // Window Managers MUST honor the win_gravity field of
		// // WM_NORMAL_HINTS for both MapRequest and ConfigureRequest events
		// // (ICCCM Version 2.0, §4.1.2.3 and §4.1.5)
		//
		// // TODO interpret win gravity (is this really needed?) and adapt x,
		// // y with & height
		// // accordingly
		//
		// // final int gravityCode = (int) sizeHintsReply.getWinGravity();
		// // final Gravity gravity = Gravity.findGravity(gravityCode);
		// //
		// window.getPreferences().getNormalHintGravity().setGravity(gravity);
		// }
		if (valueMask != 0) {
			// Some programs don't respect icccm (like firefox) and send us
			// bogus values (zero width and
			// height with an active flag).
			// We need to check for these bogus values and decide whether or not
			// to honor them.

			handleParsedValues(x, y, width, height, minWidth, minHeight,
					maxWidth, maxHeight, widthIncrement, heightIncrement);
		}
	}

	public abstract void handleParsedValues(int x, int y, int width,
			int height, int minWidth, int minHeight, int maxWidth,
			int maxHeight, int widthIncrement, int heightIncrement);
}
