package org.fusion.x11.icccm;

import java.util.HashMap;
import java.util.Map;

import org.fusion.x11.core.XAtom;
import org.fusion.x11.core.XAtomRegistry;
import org.fusion.x11.core.XDisplay;
import org.fusion.x11.core.XPropertyXAtomAtoms;
import org.fusion.x11.core.XPropertyXAtomMultiText;
import org.fusion.x11.core.XPropertyXAtomSingleText;
import org.fusion.x11.core.XPropertyXAtomWindow;
import org.fusion.x11.core.XPropertyXAtomWindows;
import org.fusion.x11.core.XProtocolConstants;

/**
 * Groups atoms defined by the ICCCM protocol.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class IcccmAtoms {
	// icccm atom names
	public static final String WM_DELETE_WINDOW_ATOM_NAME = "WM_DELETE_WINDOW";
	public static final String WM_PROTOCOLS_ATOM_NAME = "WM_PROTOCOLS";
	public static final String WM_STATE_ATOM_NAME = "WM_STATE";
	public static final String WM_TAKE_FOCUS_ATOM_NAME = "WM_TAKE_FOCUS";
	public static final String WM_CLASS_ATOM_NAME = "WM_CLASS";
	public static final String WM_CLIENT_MACHINE_ATOM_NAME = "WM_CLIENT_MACHINE";
	public static final String WM_COMMAND_ATOM_NAME = "WM_COMMAND";
	public static final String WM_HINTS_ATOM_NAME = "WM_HINTS";
	public static final String WM_ICON_NAME_ATOM_NAME = "WM_ICON_NAME";
	public static final String WM_ICON_SIZE_ATOM_NAME = "WM_ICON_SIZE";
	public static final String WM_NAME_ATOM_NAME = "WM_NAME";
	public static final String WM_SIZE_HINTS_ATOM_NAME = "WM_SIZE_HINTS";
	public static final String WM_NORMAL_HINTS_ATOM_NAME = "WM_NORMAL_HINTS";
	public static final String WM_TRANSIENT_FOR_ATOM_NAME = "WM_TRANSIENT_FOR";
	public static final String WM_ZOOM_HINTS_ATOM_NAME = "WM_ZOOM_HINTS";
	public static final String WM_CLIENT_LEADER_ATOM_NAME = "WM_CLIENT_LEADER";
	public static final String WM_COLORMAP_WINDOWS_ATOM_NAME = "WM_COLORMAP_WINDOWS";
	public static final String WM_CHANGE_STATE_ATOM_NAME = "WM_CHANGE_STATE";
	public static final String WM_COLORMAP_NOTIFY_ATOM_NAME = "WM_COLORMAP_NOTIFY";

	private static final String SCREEN_SELECTION_ATOM_NAME = "WM_S%d";
	private static final String VERSION_ATOM_NAME = "VERSION";

	public static final String getScreenSelectionAtomName(final int screenNumber) {
		return String.format(IcccmAtoms.SCREEN_SELECTION_ATOM_NAME, screenNumber);
	}

	private final XPropertyXAtomSingleText wmName;
	private final XPropertyXAtomSingleText wmIconName;
	private final WmNormalHints wmNormalHints;
	private final XAtom wmSizeHints;
	private final WmHints wmHints;
	private final XPropertyXAtomMultiText wmClass;
	private final XPropertyXAtomWindow wmTransientFor;
	private final XPropertyXAtomAtoms wmProtocols;
	private final XPropertyXAtomWindows wmColormapWindows;
	@Deprecated
	private final XAtom wmClientMachine;
	private final WmState wmState;
	private final WmIconSize wmIconSize;
	private final XAtom wmChangeState;
	private final XAtom wmTakeFocus;
	private final XAtom wmColormapNotify;
	private final XAtom wmDeleteWindow;
	@Deprecated
	private final XAtom wmCommand;
	private final XAtom wmZoomHints;
	private final XPropertyXAtomWindow wmClientLeader;

	// icccm window manager selection atoms
	private final Map<Integer, XAtom> windowManagerSelectionAtoms;
	private final Version version;

	private final XAtomRegistry atomRegistry;
	private final XDisplay display;

	public IcccmAtoms(final XDisplay display) {
		this.display = display;
		this.atomRegistry = display.getDisplayAtoms();
		// ****ICCCM ATOMS****//
		this.wmDeleteWindow = getAtomRegistry().register(
				new XAtom(getDisplay(), IcccmAtoms.WM_DELETE_WINDOW_ATOM_NAME));
		this.wmProtocols = getAtomRegistry().register(
				new XPropertyXAtomAtoms(getDisplay(),
						IcccmAtoms.WM_PROTOCOLS_ATOM_NAME));
		this.wmState = getAtomRegistry().register(new WmState(getDisplay()));
		this.wmTakeFocus = getAtomRegistry().register(
				new XAtom(getDisplay(), IcccmAtoms.WM_TAKE_FOCUS_ATOM_NAME));
		this.wmClass = getAtomRegistry().register(
				new XPropertyXAtomMultiText(getDisplay(),
						IcccmAtoms.WM_CLASS_ATOM_NAME));
		this.wmClientMachine = getAtomRegistry().register(
				new XAtom(getDisplay(), IcccmAtoms.WM_CLIENT_MACHINE_ATOM_NAME,
						Long.valueOf(XProtocolConstants.WM_CLIENT_MACHINE)));
		this.wmCommand = getAtomRegistry().register(
				new XAtom(getDisplay(), IcccmAtoms.WM_COMMAND_ATOM_NAME, Long
						.valueOf(XProtocolConstants.WM_COMMAND)));
		this.wmHints = getAtomRegistry().register(new WmHints(getDisplay()));
		this.wmIconName = getAtomRegistry().register(
				new XPropertyXAtomSingleText(getDisplay(),
						IcccmAtoms.WM_ICON_NAME_ATOM_NAME));
		this.wmIconSize = getAtomRegistry().register(
				new WmIconSize(getDisplay()));
		this.wmName = getAtomRegistry().register(
				new XPropertyXAtomSingleText(getDisplay(),
						IcccmAtoms.WM_NAME_ATOM_NAME));
		this.wmSizeHints = getAtomRegistry().register(
				new XAtom(getDisplay(), IcccmAtoms.WM_SIZE_HINTS_ATOM_NAME,
						Long.valueOf(XProtocolConstants.WM_SIZE_HINTS)));
		this.wmNormalHints = getAtomRegistry().register(
				new WmNormalHints(getDisplay()));
		this.wmTransientFor = getAtomRegistry().register(
				new XPropertyXAtomWindow(getDisplay(),
						IcccmAtoms.WM_TRANSIENT_FOR_ATOM_NAME, Long
								.valueOf(XProtocolConstants.WM_TRANSIENT_FOR)));
		this.wmZoomHints = getAtomRegistry().register(
				new XAtom(getDisplay(), IcccmAtoms.WM_ZOOM_HINTS_ATOM_NAME,
						Long.valueOf(XProtocolConstants.WM_ZOOM_HINTS)));
		this.wmClientLeader = getAtomRegistry().register(
				new XPropertyXAtomWindow(getDisplay(),
						IcccmAtoms.WM_CLIENT_LEADER_ATOM_NAME));
		this.wmColormapWindows = getAtomRegistry().register(
				new XPropertyXAtomWindows(getDisplay(),
						IcccmAtoms.WM_COLORMAP_WINDOWS_ATOM_NAME));
		this.wmChangeState = getAtomRegistry().register(
				new XAtom(getDisplay(), IcccmAtoms.WM_CHANGE_STATE_ATOM_NAME));
		this.wmColormapNotify = getAtomRegistry()
				.register(
						new XAtom(getDisplay(),
								IcccmAtoms.WM_COLORMAP_NOTIFY_ATOM_NAME));

		// icccm window manager selection atoms
		this.windowManagerSelectionAtoms = new HashMap<Integer, XAtom>();
		this.version = new Version(getDisplay(), IcccmAtoms.VERSION_ATOM_NAME);
		// *******************//
	}

	/**
	 * 
	 * @param screenNumber
	 * @return
	 */
	public XAtom getScreenSelectionAtom(final int screenNumber) {
		final String atomName = IcccmAtoms.getScreenSelectionAtomName(screenNumber);
		final Integer screen = Integer.valueOf(screenNumber);

		XAtom selectionAtom;
		if (this.windowManagerSelectionAtoms.containsKey(screen)) {
			selectionAtom = this.windowManagerSelectionAtoms.get(screen);
		} else {
			selectionAtom = getAtomRegistry().register(
					new XAtom(getDisplay(), atomName));
		}
		return selectionAtom;
	}

	public XDisplay getDisplay() {
		return this.display;
	}

	public XPropertyXAtomSingleText getWmName() {
		return this.wmName;
	}

	public XPropertyXAtomSingleText getWmIconName() {
		return this.wmIconName;
	}

	public WmNormalHints getWmNormalHints() {
		return this.wmNormalHints;
	}

	public XAtom getWmSizeHints() {
		return this.wmSizeHints;
	}

	public WmHints getWmHints() {
		return this.wmHints;
	}

	public XPropertyXAtomMultiText getWmClass() {
		return this.wmClass;
	}

	public XPropertyXAtomWindow getWmTransientFor() {
		return this.wmTransientFor;
	}

	public XPropertyXAtomAtoms getWmProtocols() {
		return this.wmProtocols;
	}

	public XPropertyXAtomWindows getWmColormapWindows() {
		return this.wmColormapWindows;
	}

	public XAtom getWmClientMachine() {
		return this.wmClientMachine;
	}

	public WmState getWmState() {
		return this.wmState;
	}

	public WmIconSize getWmIconSize() {
		return this.wmIconSize;
	}

	public XAtom getWmChangeState() {
		return this.wmChangeState;
	}

	public XAtom getWmTakeFocus() {
		return this.wmTakeFocus;
	}

	public XAtom getWmColormapNotify() {
		return this.wmColormapNotify;
	}

	public XAtom getWmDeleteWindow() {
		return this.wmDeleteWindow;
	}

	public XAtom getWmCommand() {
		return this.wmCommand;
	}

	public XAtom getWmZoomHints() {
		return this.wmZoomHints;
	}

	public XPropertyXAtomWindow getWmClientLeader() {
		return this.wmClientLeader;
	}

	public XAtomRegistry getAtomRegistry() {
		return this.atomRegistry;
	}

	public Version getVersion() {
		return this.version;
	}
}
