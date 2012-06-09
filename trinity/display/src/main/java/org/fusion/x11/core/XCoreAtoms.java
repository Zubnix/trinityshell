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
package org.fusion.x11.core;

import org.trinity.display.x11.api.XProtocolConstants;
import org.trinity.display.x11.impl.XServerImpl;

/**
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XCoreAtoms {

	// ****GENERIC ATOMS****//
	private final XAtom arc;
	private final XAtom atom;
	private final XAtom bitmap;
	private final XAtom capHeight;
	private final XAtom cardinal;
	private final XAtom colormap;
	private final XAtom copyright;
	private final XAtom cursor;
	private final XAtom cutBuffer0;
	private final XAtom cutBuffer1;
	private final XAtom cutBuffer2;
	private final XAtom cutBuffer3;
	private final XAtom cutBuffer4;
	private final XAtom cutBuffer5;
	private final XAtom cutBuffer6;
	private final XAtom cutBuffer7;
	private final XAtom drawable;
	private final XAtom endSpace;
	private final XAtom familyName;
	private final XAtom font;
	private final XAtom fontName;
	private final XAtom fullName;
	private final XAtom integer;
	private final XAtom italicAngle;
	private final XAtom maxSpace;
	private final XAtom minSpace;
	private final XAtom normSpace;
	private final XAtom notice;
	private final XAtom pixmap;
	private final XAtom point;
	private final XAtom pointSize;
	private final XAtom primary;
	private final XAtom quadWidth;
	private final XAtom rectangle;
	private final XAtom resolution;
	private final XAtom resouceManager;
	private final XAtom rgbBestMap;
	private final XAtom rgbBlueMap;
	private final XAtom rgbColorMap;
	private final XAtom rgbDefaultMap;
	private final XAtom rgbGrayMap;
	private final XAtom rgbGreenMap;
	private final XAtom rgbRedMap;
	private final XAtom secondary;
	private final XAtom strikeoutAscent;
	private final XAtom strikeoutDescent;
	private final XAtom string;
	private final XAtom subscriptX;
	private final XAtom subscriptY;
	private final XAtom superscriptX;
	private final XAtom superscriptY;
	private final XAtom underlinePosition;
	private final XAtom underlineThickness;
	private final XAtom visualId;
	private final XAtom weight;
	private final XAtom window;
	private final XAtom xHeight;
	private final XAtom utf8String;

	// *********************//
	public XCoreAtoms(final XServerImpl display) {
		final XAtomRegistry atomRegistry = display.getDisplayAtoms();
		// ****GENERIC ATOMS****//
		this.arc = atomRegistry.register(new XAtom(display, "ARC", Long
				.valueOf(XProtocolConstants.ARC)));
		this.atom = atomRegistry.register(new XAtom(display, "ATOM", Long
				.valueOf(XProtocolConstants.ATOM)));
		this.bitmap = atomRegistry.register(new XAtom(display, "BITMAP", Long
				.valueOf(XProtocolConstants.BITMAP)));
		this.capHeight = atomRegistry
				.register(new XAtom(display, "CAP_HEIGHT", Long
						.valueOf(XProtocolConstants.CAP_HEIGHT)));
		this.cardinal = atomRegistry
				.register(new XAtom(display, "CARDINAL", Long
						.valueOf(XProtocolConstants.CARDINAL)));
		this.colormap = atomRegistry
				.register(new XAtom(display, "COLORMAP", Long
						.valueOf(XProtocolConstants.COLORMAP)));
		this.copyright = atomRegistry
				.register(new XAtom(display, "COPYRIGHT", Long
						.valueOf(XProtocolConstants.COPYRIGHT)));
		this.cursor = atomRegistry.register(new XAtom(display, "CURSOR", Long
				.valueOf(XProtocolConstants.CURSOR)));
		this.cutBuffer0 = atomRegistry
				.register(new XAtom(display, "CUT_BUFFER0", Long
						.valueOf(XProtocolConstants.CUT_BUFFER0)));
		this.cutBuffer1 = atomRegistry
				.register(new XAtom(display, "CUT_BUFFER1", Long
						.valueOf(XProtocolConstants.CUT_BUFFER1)));
		this.cutBuffer2 = atomRegistry
				.register(new XAtom(display, "CUT_BUFFER2", Long
						.valueOf(XProtocolConstants.CUT_BUFFER2)));
		this.cutBuffer3 = atomRegistry
				.register(new XAtom(display, "CUT_BUFFER3", Long
						.valueOf(XProtocolConstants.CUT_BUFFER3)));
		this.cutBuffer4 = atomRegistry
				.register(new XAtom(display, "CUT_BUFFER4", Long
						.valueOf(XProtocolConstants.CUT_BUFFER4)));
		this.cutBuffer5 = atomRegistry
				.register(new XAtom(display, "CUT_BUFFER5", Long
						.valueOf(XProtocolConstants.CUT_BUFFER5)));
		this.cutBuffer6 = atomRegistry
				.register(new XAtom(display, "CUT_BUFFER6", Long
						.valueOf(XProtocolConstants.CUT_BUFFER6)));
		this.cutBuffer7 = atomRegistry
				.register(new XAtom(display, "CUT_BUFFER7", Long
						.valueOf(XProtocolConstants.CUT_BUFFER7)));
		this.drawable = atomRegistry
				.register(new XAtom(display, "DRAWABLE", Long
						.valueOf(XProtocolConstants.DRAWABLE)));
		this.endSpace = atomRegistry
				.register(new XAtom(display, "END_SPACE", Long
						.valueOf(XProtocolConstants.END_SPACE)));
		this.familyName = atomRegistry
				.register(new XAtom(display, "FAMILY_NAME", Long
						.valueOf(XProtocolConstants.FAMILY_NAME)));
		this.font = atomRegistry.register(new XAtom(display, "FONT", Long
				.valueOf(XProtocolConstants.FONT)));
		this.fontName = atomRegistry
				.register(new XAtom(display, "FONT_NAME", Long
						.valueOf(XProtocolConstants.FONT_NAME)));
		this.fullName = atomRegistry
				.register(new XAtom(display, "FULL_NAME", Long
						.valueOf(XProtocolConstants.FULL_NAME)));
		this.integer = atomRegistry.register(new XAtom(display, "INTEGER", Long
				.valueOf(XProtocolConstants.INTEGER)));
		this.italicAngle = atomRegistry
				.register(new XAtom(display, "ITALIC_ANGLE", Long
						.valueOf(XProtocolConstants.ITALIC_ANGLE)));
		this.maxSpace = atomRegistry
				.register(new XAtom(display, "MAX_SPACE", Long
						.valueOf(XProtocolConstants.MAX_SPACE)));
		this.minSpace = new XAtom(	display,
									"MIN_SPACE",
									Long.valueOf(XProtocolConstants.MIN_SPACE));
		this.normSpace = atomRegistry
				.register(new XAtom(display, "NORM_SPACE", Long
						.valueOf(XProtocolConstants.NORM_SPACE)));
		this.notice = atomRegistry.register(new XAtom(display, "NOTICE", Long
				.valueOf(XProtocolConstants.NOTICE)));
		this.pixmap = atomRegistry.register(new XAtom(display, "PIXMAP", Long
				.valueOf(XProtocolConstants.PIXMAP)));
		this.point = atomRegistry.register(new XAtom(display, "POINT", Long
				.valueOf(XProtocolConstants.POINT)));
		this.pointSize = atomRegistry
				.register(new XAtom(display, "POINT_SIZE", Long
						.valueOf(XProtocolConstants.POINT_SIZE)));
		this.primary = atomRegistry.register(new XAtom(display, "PRIMARY", Long
				.valueOf(XProtocolConstants.PRIMARY)));
		this.quadWidth = atomRegistry
				.register(new XAtom(display, "QUAD_WIDTH", Long
						.valueOf(XProtocolConstants.QUAD_WIDTH)));
		this.rectangle = atomRegistry
				.register(new XAtom(display, "RECTANGLE", Long
						.valueOf(XProtocolConstants.RECTANGLE)));
		this.resolution = atomRegistry
				.register(new XAtom(display, "RESOLUTION", Long
						.valueOf(XProtocolConstants.RESOLUTION)));
		this.resouceManager = atomRegistry
				.register(new XAtom(display, "RESOURCE_MANAGER", Long
						.valueOf(XProtocolConstants.RESOURCE_MANAGER)));
		this.rgbBestMap = atomRegistry
				.register(new XAtom(display, "RGB_BEST_MAP", Long
						.valueOf(XProtocolConstants.RGB_BEST_MAP)));
		this.rgbBlueMap = atomRegistry
				.register(new XAtom(display, "RGB_BLUE_MAP", Long
						.valueOf(XProtocolConstants.RGB_BLUE_MAP)));
		this.rgbColorMap = atomRegistry
				.register(new XAtom(display, "RGB_COLOR_MAP", Long
						.valueOf(XProtocolConstants.RGB_COLOR_MAP)));
		this.rgbDefaultMap = atomRegistry
				.register(new XAtom(display, "RGB_DEFAULT_MAP", Long
						.valueOf(XProtocolConstants.RGB_DEFAULT_MAP)));
		this.rgbGrayMap = atomRegistry
				.register(new XAtom(display, "RGB_GRAY_MAP", Long
						.valueOf(XProtocolConstants.RGB_GRAY_MAP)));
		this.rgbGreenMap = atomRegistry
				.register(new XAtom(display, "RGB_GREEN_MAP", Long
						.valueOf(XProtocolConstants.RGB_GREEN_MAP)));
		this.rgbRedMap = atomRegistry
				.register(new XAtom(display, "RGB_RED_MAP", Long
						.valueOf(XProtocolConstants.RGB_RED_MAP)));
		this.secondary = atomRegistry
				.register(new XAtom(display, "SECONDARY", Long
						.valueOf(XProtocolConstants.SECONDARY)));
		this.strikeoutAscent = atomRegistry
				.register(new XAtom(display, "STRIKEOUT_ASCENT", Long
						.valueOf(XProtocolConstants.STRIKEOUT_ASCENT)));
		this.strikeoutDescent = atomRegistry
				.register(new XAtom(display, "STRIKEOUT_DESCENT", Long
						.valueOf(XProtocolConstants.STRIKEOUT_DESCENT)));
		this.string = atomRegistry.register(new XAtom(display, "STRING", Long
				.valueOf(XProtocolConstants.STRING)));
		this.subscriptX = atomRegistry
				.register(new XAtom(display, "SUBSCRIPT_X", Long
						.valueOf(XProtocolConstants.SUBSCRIPT_X)));
		this.subscriptY = atomRegistry
				.register(new XAtom(display, "SUBSCRIPT_Y", Long
						.valueOf(XProtocolConstants.SUBSCRIPT_Y)));
		this.superscriptX = atomRegistry
				.register(new XAtom(display, "SUPERSCRIPT_X", Long
						.valueOf(XProtocolConstants.SUPERSCRIPT_X)));
		this.superscriptY = atomRegistry
				.register(new XAtom(display, "SUPERSCRIPT_Y", Long
						.valueOf(XProtocolConstants.SUPERSCRIPT_Y)));
		this.underlinePosition = atomRegistry
				.register(new XAtom(display, "UNDERLINE_POSITION", Long
						.valueOf(XProtocolConstants.UNDERLINE_POSITION)));
		this.underlineThickness = atomRegistry
				.register(new XAtom(display, "UNDERLINE_THICKNESS", Long
						.valueOf(XProtocolConstants.UNDERLINE_THICKNESS)));
		this.visualId = atomRegistry
				.register(new XAtom(display, "VISUALID", Long
						.valueOf(XProtocolConstants.VISUALID)));
		this.weight = atomRegistry.register(new XAtom(display, "WEIGHT", Long
				.valueOf(XProtocolConstants.WEIGHT)));
		this.window = atomRegistry.register(new XAtom(display, "WINDOW", Long
				.valueOf(XProtocolConstants.WINDOW)));
		this.xHeight = atomRegistry
				.register(new XAtom(display, "X_HEIGHT", Long
						.valueOf(XProtocolConstants.X_HEIGHT)));

		this.utf8String = atomRegistry.register(new XAtom(	display,
															"UTF8_STRING"));
	}

	public XAtom getArc() {
		return this.arc;
	}

	public XAtom getAtom() {
		return this.atom;
	}

	public XAtom getBitmap() {
		return this.bitmap;
	}

	public XAtom getCapHeight() {
		return this.capHeight;
	}

	public XAtom getCardinal() {
		return this.cardinal;
	}

	public XAtom getColormap() {
		return this.colormap;
	}

	public XAtom getCopyright() {
		return this.copyright;
	}

	public XAtom getCursor() {
		return this.cursor;
	}

	public XAtom getCutBuffer0() {
		return this.cutBuffer0;
	}

	public XAtom getCutBuffer1() {
		return this.cutBuffer1;
	}

	public XAtom getCutBuffer2() {
		return this.cutBuffer2;
	}

	public XAtom getCutBuffer3() {
		return this.cutBuffer3;
	}

	public XAtom getCutBuffer4() {
		return this.cutBuffer4;
	}

	public XAtom getCutBuffer5() {
		return this.cutBuffer5;
	}

	public XAtom getCutBuffer6() {
		return this.cutBuffer6;
	}

	public XAtom getCutBuffer7() {
		return this.cutBuffer7;
	}

	public XAtom getDrawable() {
		return this.drawable;
	}

	public XAtom getEndSpace() {
		return this.endSpace;
	}

	public XAtom getFamilyName() {
		return this.familyName;
	}

	public XAtom getFont() {
		return this.font;
	}

	public XAtom getFontName() {
		return this.fontName;
	}

	public XAtom getFullName() {
		return this.fullName;
	}

	public XAtom getInteger() {
		return this.integer;
	}

	public XAtom getItalicAngle() {
		return this.italicAngle;
	}

	public XAtom getMaxSpace() {
		return this.maxSpace;
	}

	public XAtom getMinSpace() {
		return this.minSpace;
	}

	public XAtom getNormSpace() {
		return this.normSpace;
	}

	public XAtom getNotice() {
		return this.notice;
	}

	public XAtom getPixmap() {
		return this.pixmap;
	}

	public XAtom getPoint() {
		return this.point;
	}

	public XAtom getPointSize() {
		return this.pointSize;
	}

	public XAtom getPrimary() {
		return this.primary;
	}

	public XAtom getQuadWidth() {
		return this.quadWidth;
	}

	public XAtom getRectangle() {
		return this.rectangle;
	}

	public XAtom getResolution() {
		return this.resolution;
	}

	public XAtom getResouceManager() {
		return this.resouceManager;
	}

	public XAtom getRgbBestMap() {
		return this.rgbBestMap;
	}

	public XAtom getRgbBlueMap() {
		return this.rgbBlueMap;
	}

	public XAtom getRgbColorMap() {
		return this.rgbColorMap;
	}

	public XAtom getRgbDefaultMap() {
		return this.rgbDefaultMap;
	}

	public XAtom getRgbGrayMap() {
		return this.rgbGrayMap;
	}

	public XAtom getRgbGreenMap() {
		return this.rgbGreenMap;
	}

	public XAtom getRgbRedMap() {
		return this.rgbRedMap;
	}

	public XAtom getSecondary() {
		return this.secondary;
	}

	public XAtom getStrikeoutAscent() {
		return this.strikeoutAscent;
	}

	public XAtom getStrikeoutDescent() {
		return this.strikeoutDescent;
	}

	public XAtom getString() {
		return this.string;
	}

	public XAtom getSubscriptX() {
		return this.subscriptX;
	}

	public XAtom getSubscriptY() {
		return this.subscriptY;
	}

	public XAtom getSuperscriptX() {
		return this.superscriptX;
	}

	public XAtom getSuperscriptY() {
		return this.superscriptY;
	}

	public XAtom getUnderlinePosition() {
		return this.underlinePosition;
	}

	public XAtom getUnderlineThickness() {
		return this.underlineThickness;
	}

	public XAtom getVisualId() {
		return this.visualId;
	}

	public XAtom getWeight() {
		return this.weight;
	}

	public XAtom getWindow() {
		return this.window;
	}

	public XAtom getxHeight() {
		return this.xHeight;
	}

	public XAtom getUtf8String() {
		return this.utf8String;
	}
}
