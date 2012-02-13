package org.hypercube.protocol;

public class IconPreferences implements ProtocolEventArguments {
	private final byte[] iconData;
	private final int iconWidth;
	private final int iconHeight;

	public IconPreferences(final byte[] iconDate, final int iconWidth,
			final int iconHeight) {
		this.iconData = iconDate;
		this.iconWidth = iconWidth;
		this.iconHeight = iconHeight;
	}

	public byte[] getIconData() {
		return this.iconData;
	}

	public int getIconWidth() {
		return this.iconWidth;
	}

	public int getIconHeight() {
		return this.iconHeight;
	}
}
