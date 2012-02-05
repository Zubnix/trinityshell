package org.fusion.x11.ewmh;

import org.fusion.x11.core.XAtom;
import org.fusion.x11.core.XAtomRegistry;
import org.fusion.x11.core.XDisplay;
import org.fusion.x11.core.XPropertyXAtomAtoms;
import org.fusion.x11.core.XPropertyXAtomCardinal;
import org.fusion.x11.core.XPropertyXAtomCardinals;
import org.fusion.x11.core.XPropertyXAtomMultiText;
import org.fusion.x11.core.XPropertyXAtomSingleText;
import org.fusion.x11.core.XPropertyXAtomWindow;
import org.fusion.x11.core.XPropertyXAtomWindows;

// TODO move the seperate file in EWMH package
/**
 * Groups atoms defined by the EWMH protocol.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class EwmhAtoms {
	// TODO group all ewmh atom names as static constants here.
	public static final String NET_SUPPORTED_ATOM_NAME = "_NET_SUPPORTED";
	public static final String NET_CLIENT_LIST_ATOM_NAME = "_NET_CLIENT_LIST";
	public static final String NET_CLIENT_LIST_STACKING_ATOM_NAME = "_NET_CLIENT_LIST_STACKING";
	public static final String NET_NUMBER_OF_DESKTOPS_ATOM_NAME = "_NET_NUMBER_OF_DESKTOPS";
	public static final String NET_DESKTOP_GEOMETRY_ATOM_NAME = "_NET_DESKTOP_GEOMETRY";
	public static final String NET_DESKTOP_VIEWPORT_ATOM_NAME = "_NET_DESKTOP_VIEWPORT";
	public static final String NET_WORKAREA_ATOM_NAME = "_NET_WORKAREA";
	public static final String NET_CURRENT_DESKTOP_ATOM_NAME = "_NET_CURRENT_DESKTOP";
	public static final String NET_DESKTOP_NAMES_ATOM_NAME = "_NET_DESKTOP_NAMES";
	public static final String NET_ACTIVE_WINDOW_ATOM_NAME = "_NET_ACTIVE_WINDOW";
	public static final String NET_SUPPORTING_WM_CHECK_ATOM_NAME = "_NET_SUPPORTING_WM_CHECK";
	public static final String NET_VIRTUAL_ROOTS_ATOM_NAME = "_NET_VIRTUAL_ROOTS";
	public static final String NET_DESKTOP_LAYOUT_ATOM_NAME = "_NET_DESKTOP_LAYOUT";
	public static final String NET_SHOWING_DESKTOP_ATOM_NAME = "_NET_SHOWING_DESKTOP";
	public static final String NET_CLOSE_WINDOW_ATOM_NAME = "_NET_CLOSE_WINDOW";
	public static final String NET_MOVERESIZE_WINDOW_ATOM_NAME = "_NET_MOVERESIZE_WINDOW";
	public static final String NET_WM_MOVERESIZE_ATOM_NAME = "_NET_WM_MOVERESIZE";
	public static final String NET_RESTACK_WINDOW_ATOM_NAME = "_NET_RESTACK_WINDOW";
	public static final String NET_REQUEST_FRAME_EXTENTS_ATOM_NAME = "_NET_REQUEST_FRAME_EXTENTS";
	public static final String NET_WM_NAME_ATOM_NAME = "_NET_WM_NAME";
	public static final String NET_WM_VISIBLE_NAME_ATOM_NAME = "_NET_WM_VISIBLE_NAME";
	public static final String NET_WM_ICON_NAME_ATOM_NAME = "_NET_WM_ICON_NAME";
	public static final String NET_WM_VISIBLE_ICON_NAME_ATOM_NAME = "_NET_WM_VISIBLE_ICON_NAME";
	public static final String NET_WM_DESKTOP_ATOM_NAME = "_NET_WM_DESKTOP";
	public static final String NET_WM_WINDOW_TYPE_ATOM_NAME = "_NET_WM_WINDOW_TYPE";
	public static final String NET_WM_WINDOW_TYPE_DESKTOP_ATOM_NAME = "_NET_WM_WINDOW_TYPE_DESKTOP";
	public static final String NET_WM_WINDOW_TYPE_DOCK_ATOM_NAME = "_NET_WM_WINDOW_TYPE_DOCK";
	public static final String NET_WM_WINDOW_TYPE_TOOLBAR_ATOM_NAME = "_NET_WM_WINDOW_TYPE_TOOLBAR";
	public static final String NET_WM_WINDOW_TYPE_MENU_ATOM_NAME = "_NET_WM_WINDOW_TYPE_MENU";
	public static final String NET_WM_WINDOW_TYPE_UTILITY_ATOM_NAME = "_NET_WM_WINDOW_TYPE_UTILITY";
	public static final String NET_WM_WINDOW_TYPE_SPLASH_ATOM_NAME = "_NET_WM_WINDOW_TYPE_SPLASH";
	public static final String NET_WM_WINDOW_TYPE_DIALOG_ATOM_NAME = "_NET_WM_WINDOW_TYPE_DIALOG";
	public static final String NET_WM_WINDOW_TYPE_NORMAL_ATOM_NAME = "_NET_WM_WINDOW_TYPE_NORMAL";
	public static final String NET_WM_STATE_ATOM_NAME = "_NET_WM_STATE";
	public static final String NET_WM_STATE_MODEL_ATOM_NAME = "_NET_WM_STATE_MODEL";
	public static final String NET_WM_STATE_STICKY_ATOM_NAME = "_NET_WM_STATE_STICKY";
	public static final String NET_WM_STATE_MAXIMIZED_VERT_ATOM_NAME = "_NET_WM_STATE_MAXIMIZED_VERT";
	public static final String NET_WM_STATE_MAXIMIZED_HORZ_ATOM_NAME = "_NET_WM_STATE_MAXIMIZED_HORZ";
	public static final String NET_WM_STATE_SHADED_ATOM_NAME = "_NET_WM_STATE_SHADED";
	public static final String NET_WM_STATE_SKIP_TASKBAR_ATOM_NAME = "_NET_WM_STATE_SKIP_TASKBAR";
	public static final String NET_WM_STATE_SKIP_PAGER_ATOM_NAME = "_NET_WM_STATE_SKIP_PAGER";
	public static final String NET_WM_STATE_HIDDEN_ATOM_NAME = "_NET_WM_STATE_HIDDEN";
	public static final String NET_WM_STATE_FULLSCREEN_ATOM_NAME = "_NET_WM_STATE_FULLSCREEN";
	public static final String NET_WM_STATE_ABOVE_ATOM_NAME = "_NET_WM_STATE_ABOVE";
	public static final String NET_WM_STATE_BELOW_ATOM_NAME = "_NET_WM_STATE_BELOW";
	public static final String NET_WM_STATE_DEMANDS_ATTENTION_ATOM_NAME = "_NET_WM_STATE_DEMANDS_ATTENTION";
	public static final String NET_WM_ALLOWED_ACTIONS_ATOM_NAME = "_NET_WM_ALLOWED_ACTIONS";
	public static final String NET_WM_ACTION_MOVE_ATOM_NAME = "_NET_WM_ACTION_MOVE";
	public static final String NET_WM_ACTION_RESIZE_ATOM_NAME = "_NET_WM_ACTION_RESIZE";
	public static final String NET_WM_ACTION_MINIMIZE_ATOM_NAME = "_NET_WM_ACTION_MINIMIZE";
	public static final String NET_WM_ACTION_SHADE_ATOM_NAME = "_NET_WM_ACTION_SHADE";
	public static final String NET_WM_ACTION_STICK_ATOM_NAME = "_NET_WM_ACTION_STICK";
	public static final String NET_WM_ACTION_MAXIMIZE_HORZ_ATOM_NAME = "_NET_WM_ACTION_MAXIMIZE_HORZ";
	public static final String NET_WM_ACTION_MAXIMIZE_VERT_ATOM_NAME = "_NET_WM_ACTION_MAXIMIZE_VERT";
	public static final String NET_WM_ACTION_FULLSCREEN_ATOM_NAME = "_NET_WM_ACTION_FULLSCREEN";
	public static final String NET_WM_ACTION_CHANGE_DESKTOP_ATOM_NAME = "_NET_WM_ACTION_CHANGE_DESKTOP";
	public static final String NET_WM_ACTION_CLOSE_ATOM_NAME = "_NET_WM_ACTION_CLOSE";
	public static final String NET_WM_STRUT_ATOM_NAME = "_NET_WM_STRUT";
	public static final String NET_WM_STRUT_PARTIAL_ATOM_NAME = "_NET_WM_STRUT_PARTIAL";
	public static final String NET_WM_ICON_GEOMETRY_ATOM_NAME = "_NET_WM_ICON_GEOMETRY";
	public static final String NET_WM_PID_ATOM_NAME = "_NET_WM_PID";
	public static final String NET_WM_USER_TIME_ATOM_NAME = "_NET_WM_USER_TIME";
	public static final String NET_WM_USER_TIME_WINDOW_ATOM_NAME = "_NET_WM_USER_TIME_WINDOW";
	public static final String NET_FRAME_EXTENTS_ATOM_NAME = "_NET_FRAME_EXTENTS";
	public static final String NET_WM_PING_ATOM_NAME = "_NET_WM_PING";
	public static final String NET_WM_SYNC_REQUEST_ATOM_NAME = "_NET_WM_SYNC_REQUEST";
	public static final String NET_WM_SYNC_REQUEST_COUNTER_ATOM_NAME = "NET_WM_SYNC_REQUEST_COUNTER_ATOM_NAME";
	public static final String NET_WM_FULLSCREEN_MONITORS_ATOM_NAME = "_NET_WM_FULLSCREEN_MONITORS";
	public static final String NET_WM_FULL_PLACEMENT_ATOM_NAME = "_NET_WM_FULL_PLACEMENT";

	// Root Window DisplayAtoms (and Related Messages)
	private final XPropertyXAtomAtoms netSupported;
	private final XPropertyXAtomWindows netClientList;
	private final XPropertyXAtomWindows netClientListStacking;
	private final XPropertyXAtomCardinal netNumberOfDesktops;
	private final XPropertyXAtomCardinals netDesktopGeometry;
	private final _NetDesktopViewPort netDesktopViewport;
	private final XPropertyXAtomCardinal netCurrentDesktop;
	private final XPropertyXAtomMultiText netDesktopNames;
	private final XPropertyXAtomWindow netActiveWindow;
	private final _NetWorkArea netWorkarea;
	private final XPropertyXAtomWindow netSupportingWmCheck;
	private final XPropertyXAtomWindows netVirtualRoots;
	private final _NetDesktopLayout netDesktopLayout;
	private final _NetShowingDesktop netShowingDesktop;

	// Other Root Window Messages
	private final XAtom netCloseWindow;
	private final XAtom netMoveresizeWindow;
	private final XAtom netWmMoveresize;
	private final XAtom netRestackWindow;
	private final XAtom netRequestFrameExtents;

	// Application Window DisplayAtoms
	private final XPropertyXAtomSingleText netWmName;
	private final XPropertyXAtomSingleText netWmVisibleName;
	private final XPropertyXAtomSingleText netWmIconName;
	private final XPropertyXAtomSingleText netWmVisibleIconName;
	private final XPropertyXAtomCardinal netWmDesktop;
	private final XPropertyXAtomAtoms netWmWindowType;
	private final XAtom netWmWindowTypeDesktop;
	private final XAtom netWmWindowTypeDock;
	private final XAtom netWmWindowTypeToolbar;
	private final XAtom netWmWindowTypeMenu;
	private final XAtom netWmWindowTypeUtility;
	private final XAtom netWmWindowTypeSplash;
	private final XAtom netWmWindowTypeDialog;
	private final XAtom netWmWindowTypeNormal;
	private final XPropertyXAtomAtoms netWmState;
	private final XAtom netWmStateModel;
	private final XAtom netWmStateSticky;
	private final XAtom netWmStateMaximizedVert;
	private final XAtom netWmStateMaximizedHorz;
	private final XAtom netWmStateShaded;
	private final XAtom netWmStateSkipTaskbar;
	private final XAtom netWmStateSkipPager;
	private final XAtom netWmStateHidden;
	private final XAtom netWmStateFullscreen;
	private final XAtom netWmStateAbove;
	private final XAtom netWmStateBelow;
	private final XAtom netWmStateDemandsAttention;
	private final XPropertyXAtomAtoms netWmAllowedActions;
	private final XAtom netWmActionMove;
	private final XAtom netWmActionResize;
	private final XAtom netWmActionMinimize;
	private final XAtom netWmActionShade;
	private final XAtom netWmActionSticky;
	private final XAtom netWmActionMaximizeHorz;
	private final XAtom netWmActionMaximizeVert;
	private final XAtom netWmActionFullscreen;
	private final XAtom netWmActionChangeDesktop;
	private final XAtom netWmActionClose;
	private final XPropertyXAtomCardinals netWmStrut;
	private final XPropertyXAtomCardinals netWmStrutPartial;
	private final XPropertyXAtomCardinals netWmIconGeometry;
	private final _NetWmIcon netWmIcon;
	private final XPropertyXAtomCardinal netWmPid;
	private final _NetWmHandledIcons netWmHandledIcons;
	private final XPropertyXAtomCardinal netWmUserTime;
	private final XPropertyXAtomWindow netWmUserTimeWindow;
	private final XPropertyXAtomCardinals netFrameExtents;

	// Window Manager Protocols
	private final XAtom netWmPing;
	private final XAtom netWmSyncRequest;
	private final _NetWmSyncRequestCounter netWmSyncRequestCounter;
	private final XPropertyXAtomCardinals netWmFullscreenMonitors;

	// Other DisplayAtoms
	private final XAtom netWmFullPlacement;

	// TODO more

	public EwmhAtoms(final XDisplay display) {
		final XAtomRegistry atomRegistry = display.getDisplayAtoms();
		// ****EWMH ATOMS****//
		// Root Window DisplayAtoms (and Related Messages)
		this.netSupported = atomRegistry.register(new XPropertyXAtomAtoms(
				display, NET_SUPPORTED_ATOM_NAME));
		this.netClientList = atomRegistry.register(new XPropertyXAtomWindows(
				display, NET_CLIENT_LIST_ATOM_NAME));
		this.netClientListStacking = atomRegistry
				.register(new XPropertyXAtomWindows(display,
						NET_CLIENT_LIST_STACKING_ATOM_NAME));
		this.netNumberOfDesktops = atomRegistry
				.register(new XPropertyXAtomCardinal(display,
						NET_NUMBER_OF_DESKTOPS_ATOM_NAME));
		this.netDesktopGeometry = atomRegistry
				.register(new XPropertyXAtomCardinals(display,
						NET_DESKTOP_GEOMETRY_ATOM_NAME, 2));
		this.netDesktopViewport = atomRegistry
				.register(new _NetDesktopViewPort(display));
		this.netCurrentDesktop = atomRegistry
				.register(new XPropertyXAtomCardinal(display,
						NET_CURRENT_DESKTOP_ATOM_NAME));
		this.netDesktopNames = atomRegistry
				.register(new XPropertyXAtomMultiText(display,
						NET_DESKTOP_NAMES_ATOM_NAME));
		this.netActiveWindow = atomRegistry.register(new XPropertyXAtomWindow(
				display, NET_ACTIVE_WINDOW_ATOM_NAME));
		this.netWorkarea = atomRegistry.register(new _NetWorkArea(display));
		this.netSupportingWmCheck = atomRegistry
				.register(new XPropertyXAtomWindow(display,
						NET_SUPPORTING_WM_CHECK_ATOM_NAME));
		this.netVirtualRoots = atomRegistry.register(new XPropertyXAtomWindows(
				display, NET_VIRTUAL_ROOTS_ATOM_NAME));
		this.netDesktopLayout = atomRegistry.register(new _NetDesktopLayout(
				display));
		this.netShowingDesktop = atomRegistry.register(new _NetShowingDesktop(
				display));

		// Other Root Window Messages
		this.netCloseWindow = atomRegistry.register(new XAtom(display,
				NET_CLOSE_WINDOW_ATOM_NAME));
		this.netMoveresizeWindow = atomRegistry.register(new XAtom(display,
				NET_MOVERESIZE_WINDOW_ATOM_NAME));
		this.netWmMoveresize = atomRegistry.register(new XAtom(display,
				NET_WM_MOVERESIZE_ATOM_NAME));
		this.netRestackWindow = atomRegistry.register(new XAtom(display,
				NET_RESTACK_WINDOW_ATOM_NAME));
		this.netRequestFrameExtents = atomRegistry.register(new XAtom(display,
				NET_REQUEST_FRAME_EXTENTS_ATOM_NAME));

		// Application Window DisplayAtoms
		this.netWmName = atomRegistry.register(new XPropertyXAtomSingleText(
				display, NET_WM_NAME_ATOM_NAME));
		this.netWmVisibleName = atomRegistry
				.register(new XPropertyXAtomSingleText(display,
						NET_WM_VISIBLE_NAME_ATOM_NAME));
		this.netWmIconName = atomRegistry
				.register(new XPropertyXAtomSingleText(display,
						NET_WM_ICON_NAME_ATOM_NAME));
		this.netWmVisibleIconName = atomRegistry
				.register(new XPropertyXAtomSingleText(display,
						NET_WM_VISIBLE_ICON_NAME_ATOM_NAME));
		this.netWmDesktop = atomRegistry.register(new XPropertyXAtomCardinal(
				display, NET_WM_DESKTOP_ATOM_NAME));
		this.netWmWindowType = atomRegistry.register(new XPropertyXAtomAtoms(
				display, NET_WM_WINDOW_TYPE_ATOM_NAME));
		this.netWmWindowTypeDesktop = atomRegistry.register(new XAtom(display,
				NET_WM_WINDOW_TYPE_DESKTOP_ATOM_NAME));
		this.netWmWindowTypeDock = atomRegistry.register(new XAtom(display,
				NET_WM_WINDOW_TYPE_DOCK_ATOM_NAME));
		this.netWmWindowTypeToolbar = atomRegistry.register(new XAtom(display,
				NET_WM_WINDOW_TYPE_TOOLBAR_ATOM_NAME));
		this.netWmWindowTypeMenu = atomRegistry.register(new XAtom(display,
				NET_WM_WINDOW_TYPE_MENU_ATOM_NAME));
		this.netWmWindowTypeUtility = atomRegistry.register(new XAtom(display,
				NET_WM_WINDOW_TYPE_UTILITY_ATOM_NAME));
		this.netWmWindowTypeSplash = atomRegistry.register(new XAtom(display,
				NET_WM_WINDOW_TYPE_SPLASH_ATOM_NAME));
		this.netWmWindowTypeDialog = atomRegistry.register(new XAtom(display,
				NET_WM_WINDOW_TYPE_DIALOG_ATOM_NAME));
		this.netWmWindowTypeNormal = atomRegistry.register(new XAtom(display,
				NET_WM_WINDOW_TYPE_NORMAL_ATOM_NAME));
		this.netWmState = atomRegistry.register(new XPropertyXAtomAtoms(
				display, NET_WM_STATE_ATOM_NAME));
		this.netWmStateModel = atomRegistry.register(new XAtom(display,
				NET_WM_STATE_MODEL_ATOM_NAME));
		this.netWmStateSticky = atomRegistry.register(new XAtom(display,
				NET_WM_STATE_STICKY_ATOM_NAME));
		this.netWmStateMaximizedVert = atomRegistry.register(new XAtom(display,
				NET_WM_STATE_MAXIMIZED_VERT_ATOM_NAME));
		this.netWmStateMaximizedHorz = atomRegistry.register(new XAtom(display,
				NET_WM_STATE_MAXIMIZED_HORZ_ATOM_NAME));
		this.netWmStateShaded = atomRegistry.register(new XAtom(display,
				NET_WM_STATE_SHADED_ATOM_NAME));
		this.netWmStateSkipTaskbar = atomRegistry.register(new XAtom(display,
				NET_WM_STATE_SKIP_TASKBAR_ATOM_NAME));
		this.netWmStateSkipPager = atomRegistry.register(new XAtom(display,
				NET_WM_STATE_SKIP_PAGER_ATOM_NAME));
		this.netWmStateHidden = atomRegistry.register(new XAtom(display,
				NET_WM_STATE_HIDDEN_ATOM_NAME));
		this.netWmStateFullscreen = atomRegistry.register(new XAtom(display,
				NET_WM_STATE_FULLSCREEN_ATOM_NAME));
		this.netWmStateAbove = atomRegistry.register(new XAtom(display,
				NET_WM_STATE_ABOVE_ATOM_NAME));
		this.netWmStateBelow = atomRegistry.register(new XAtom(display,
				NET_WM_STATE_BELOW_ATOM_NAME));
		this.netWmStateDemandsAttention = atomRegistry.register(new XAtom(
				display, NET_WM_STATE_DEMANDS_ATTENTION_ATOM_NAME));
		this.netWmAllowedActions = atomRegistry
				.register(new XPropertyXAtomAtoms(display,
						NET_WM_ALLOWED_ACTIONS_ATOM_NAME));
		this.netWmActionMove = atomRegistry.register(new XAtom(display,
				NET_WM_ACTION_MOVE_ATOM_NAME));
		this.netWmActionResize = atomRegistry.register(new XAtom(display,
				NET_WM_ACTION_RESIZE_ATOM_NAME));
		this.netWmActionMinimize = atomRegistry.register(new XAtom(display,
				NET_WM_ACTION_MINIMIZE_ATOM_NAME));
		this.netWmActionShade = atomRegistry.register(new XAtom(display,
				NET_WM_ACTION_SHADE_ATOM_NAME));
		this.netWmActionSticky = atomRegistry.register(new XAtom(display,
				NET_WM_ACTION_STICK_ATOM_NAME));
		this.netWmActionMaximizeHorz = atomRegistry.register(new XAtom(display,
				NET_WM_ACTION_MAXIMIZE_HORZ_ATOM_NAME));
		this.netWmActionMaximizeVert = atomRegistry.register(new XAtom(display,
				NET_WM_ACTION_MAXIMIZE_VERT_ATOM_NAME));
		this.netWmActionFullscreen = atomRegistry.register(new XAtom(display,
				NET_WM_ACTION_FULLSCREEN_ATOM_NAME));
		this.netWmActionChangeDesktop = atomRegistry.register(new XAtom(
				display, NET_WM_ACTION_CHANGE_DESKTOP_ATOM_NAME));
		this.netWmActionClose = atomRegistry.register(new XAtom(display,
				NET_WM_ACTION_CLOSE_ATOM_NAME));
		this.netWmStrut = atomRegistry.register(new XPropertyXAtomCardinals(
				display, NET_WM_STRUT_ATOM_NAME, 4));
		this.netWmStrutPartial = atomRegistry
				.register(new XPropertyXAtomCardinals(display,
						NET_WM_STRUT_PARTIAL_ATOM_NAME, 12));
		this.netWmIconGeometry = atomRegistry
				.register(new XPropertyXAtomCardinals(display,
						NET_WM_ICON_GEOMETRY_ATOM_NAME, 4));
		this.netWmIcon = atomRegistry.register(new _NetWmIcon(display));
		this.netWmPid = atomRegistry.register(new XPropertyXAtomCardinal(
				display, NET_WM_PID_ATOM_NAME));
		this.netWmHandledIcons = atomRegistry.register(new _NetWmHandledIcons(
				display));
		this.netWmUserTime = atomRegistry.register(new XPropertyXAtomCardinal(
				display, NET_WM_USER_TIME_ATOM_NAME));
		this.netWmUserTimeWindow = atomRegistry
				.register(new XPropertyXAtomWindow(display,
						NET_WM_USER_TIME_WINDOW_ATOM_NAME));
		this.netFrameExtents = atomRegistry
				.register(new XPropertyXAtomCardinals(display,
						NET_FRAME_EXTENTS_ATOM_NAME, 4));

		// Window Manager Protocols
		this.netWmPing = atomRegistry.register(new XAtom(display,
				NET_WM_PING_ATOM_NAME));
		this.netWmSyncRequest = atomRegistry.register(new XAtom(display,
				NET_WM_SYNC_REQUEST_ATOM_NAME));
		this.netWmSyncRequestCounter = atomRegistry
				.register(new _NetWmSyncRequestCounter(display));
		this.netWmFullscreenMonitors = atomRegistry
				.register(new XPropertyXAtomCardinals(display,
						NET_WM_FULLSCREEN_MONITORS_ATOM_NAME, 4));

		// Other DisplayAtoms
		this.netWmFullPlacement = atomRegistry.register(new XAtom(display,
				NET_WM_FULL_PLACEMENT_ATOM_NAME));
		// TODO more

		// ******************//
	}

	public XPropertyXAtomAtoms getNetSupported() {
		return this.netSupported;
	}

	public XPropertyXAtomWindows getNetClientList() {
		return this.netClientList;
	}

	public XPropertyXAtomWindows getNetClientListStacking() {
		return this.netClientListStacking;
	}

	public XPropertyXAtomCardinal getNetNumberOfDesktops() {
		return this.netNumberOfDesktops;
	}

	public XPropertyXAtomCardinals getNetDesktopGeometry() {
		return this.netDesktopGeometry;
	}

	public _NetDesktopViewPort getNetDesktopViewport() {
		return this.netDesktopViewport;
	}

	public XPropertyXAtomCardinal getNetCurrentDesktop() {
		return this.netCurrentDesktop;
	}

	public XPropertyXAtomMultiText getNetDesktopNames() {
		return this.netDesktopNames;
	}

	public XPropertyXAtomWindow getNetActiveWindow() {
		return this.netActiveWindow;
	}

	public _NetWorkArea getNetWorkarea() {
		return this.netWorkarea;
	}

	public XPropertyXAtomWindow getNetSupportingWmCheck() {
		return this.netSupportingWmCheck;
	}

	public XPropertyXAtomWindows getNetVirtualRoots() {
		return this.netVirtualRoots;
	}

	public _NetDesktopLayout getNetDesktopLayout() {
		return this.netDesktopLayout;
	}

	public _NetShowingDesktop getNetShowingDesktop() {
		return this.netShowingDesktop;
	}

	public XAtom getNetCloseWindow() {
		return this.netCloseWindow;
	}

	public XAtom getNetMoveresizeWindow() {
		return this.netMoveresizeWindow;
	}

	public XAtom getNetWmMoveresize() {
		return this.netWmMoveresize;
	}

	public XAtom getNetRestackWindow() {
		return this.netRestackWindow;
	}

	public XAtom getNetRequestFrameExtents() {
		return this.netRequestFrameExtents;
	}

	public XPropertyXAtomSingleText getNetWmName() {
		return this.netWmName;
	}

	public XPropertyXAtomSingleText getNetWmVisibleName() {
		return this.netWmVisibleName;
	}

	public XPropertyXAtomSingleText getNetWmIconName() {
		return this.netWmIconName;
	}

	public XPropertyXAtomSingleText getNetWmVisibleIconName() {
		return this.netWmVisibleIconName;
	}

	public XPropertyXAtomCardinal getNetWmDesktop() {
		return this.netWmDesktop;
	}

	public XPropertyXAtomAtoms getNetWmWindowType() {
		return this.netWmWindowType;
	}

	public XAtom getNetWmWindowTypeDesktop() {
		return this.netWmWindowTypeDesktop;
	}

	public XAtom getNetWmWindowTypeDock() {
		return this.netWmWindowTypeDock;
	}

	public XAtom getNetWmWindowTypeToolbar() {
		return this.netWmWindowTypeToolbar;
	}

	public XAtom getNetWmWindowTypeMenu() {
		return this.netWmWindowTypeMenu;
	}

	public XAtom getNetWmWindowTypeUtility() {
		return this.netWmWindowTypeUtility;
	}

	public XAtom getNetWmWindowTypeSplash() {
		return this.netWmWindowTypeSplash;
	}

	public XAtom getNetWmWindowTypeDialog() {
		return this.netWmWindowTypeDialog;
	}

	public XAtom getNetWmWindowTypeNormal() {
		return this.netWmWindowTypeNormal;
	}

	public XPropertyXAtomAtoms getNetWmState() {
		return this.netWmState;
	}

	public XAtom getNetWmStateModel() {
		return this.netWmStateModel;
	}

	public XAtom getNetWmStateSticky() {
		return this.netWmStateSticky;
	}

	public XAtom getNetWmStateMaximizedVert() {
		return this.netWmStateMaximizedVert;
	}

	public XAtom getNetWmStateMaximizedHorz() {
		return this.netWmStateMaximizedHorz;
	}

	public XAtom getNetWmStateShaded() {
		return this.netWmStateShaded;
	}

	public XAtom getNetWmStateSkipTaskbar() {
		return this.netWmStateSkipTaskbar;
	}

	public XAtom getNetWmStateSkipPager() {
		return this.netWmStateSkipPager;
	}

	public XAtom getNetWmStateHidden() {
		return this.netWmStateHidden;
	}

	public XAtom getNetWmStateFullscreen() {
		return this.netWmStateFullscreen;
	}

	public XAtom getNetWmStateAbove() {
		return this.netWmStateAbove;
	}

	public XAtom getNetWmStateBelow() {
		return this.netWmStateBelow;
	}

	public XAtom getNetWmStateDemandsAttention() {
		return this.netWmStateDemandsAttention;
	}

	public XPropertyXAtomAtoms getNetWmAllowedActions() {
		return this.netWmAllowedActions;
	}

	public XAtom getNetWmActionMove() {
		return this.netWmActionMove;
	}

	public XAtom getNetWmActionResize() {
		return this.netWmActionResize;
	}

	public XAtom getNetWmActionMinimize() {
		return this.netWmActionMinimize;
	}

	public XAtom getNetWmActionShade() {
		return this.netWmActionShade;
	}

	public XAtom getNetWmActionSticky() {
		return this.netWmActionSticky;
	}

	public XAtom getNetWmActionMaximizeHorz() {
		return this.netWmActionMaximizeHorz;
	}

	public XAtom getNetWmActionMaximizeVert() {
		return this.netWmActionMaximizeVert;
	}

	public XAtom getNetWmActionFullscreen() {
		return this.netWmActionFullscreen;
	}

	public XAtom getNetWmActionChangeDesktop() {
		return this.netWmActionChangeDesktop;
	}

	public XAtom getNetWmActionClose() {
		return this.netWmActionClose;
	}

	public XPropertyXAtomCardinals getNetWmStrut() {
		return this.netWmStrut;
	}

	public XPropertyXAtomCardinals getNetWmStrutPartial() {
		return this.netWmStrutPartial;
	}

	public XPropertyXAtomCardinals getNetWmIconGeometry() {
		return this.netWmIconGeometry;
	}

	public _NetWmIcon getNetWmIcon() {
		return this.netWmIcon;
	}

	public XPropertyXAtomCardinal getNetWmPid() {
		return this.netWmPid;
	}

	public _NetWmHandledIcons getNetWmHandledIcons() {
		return this.netWmHandledIcons;
	}

	public XPropertyXAtomCardinal getNetWmUserTime() {
		return this.netWmUserTime;
	}

	public XPropertyXAtomWindow getNetWmUserTimeWindow() {
		return this.netWmUserTimeWindow;
	}

	public XPropertyXAtomCardinals getNetFrameExtents() {
		return this.netFrameExtents;
	}

	public XAtom getNetWmPing() {
		return this.netWmPing;
	}

	public XAtom getNetWmSyncRequest() {
		return this.netWmSyncRequest;
	}

	public _NetWmSyncRequestCounter getNetWmSyncRequestCounter() {
		return this.netWmSyncRequestCounter;
	}

	public XPropertyXAtomCardinals getNetWmFullscreenMonitors() {
		return this.netWmFullscreenMonitors;
	}

	public XAtom getNetWmFullPlacement() {
		return this.netWmFullPlacement;
	}
}
