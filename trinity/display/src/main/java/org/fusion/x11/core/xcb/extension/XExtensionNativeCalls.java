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
package org.fusion.x11.core.xcb.extension;

import org.fusion.x11.core.extension.XExtensionNative;
import org.trinity.display.x11.impl.xcb.AbstractXcbCall;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;

// TODO documentation
// currently unused
/**
 * @author Erik De Rijcke
 * @since 1.1
 */
public final class XExtensionNativeCalls {
	// ****START XDAMAGE****//

	// TODO args doc
	public XDamageInit getCallXDamageInit() {
		return xDamageInit;
	}

	private final XDamageInit xDamageInit;

	public final class XDamageInit extends
			AbstractXcbCall<XcbExtensionVersionReply, Long, Integer> {
		private XDamageInit() {
		}

		@Override
		public XcbExtensionVersionReply getResult() {
			return XExtensionNativeCalls.this
					.readVersionReply(getNativeBufferHelper());
		}

		@Override
		protected boolean callImpl() {
			final int majorVersion = getArgs()[0].intValue();
			final int minorVersion = getArgs()[1].intValue();
			final boolean error = XExtensionNative.nativeXDamageInit(
					getConnectionReference().longValue(), majorVersion, minorVersion,
					getNativeBufferHelper().getBuffer());
			return error;
		}
	}

	// TODO args doc
	public XDamageCreate getCallXDamageCreate() {
		return xDamageCreate;
	}

	private final XDamageCreate xDamageCreate;

	public final class XDamageCreate extends AbstractXcbCall<Long, Long, Number> {
		private XDamageCreate() {
		}

		@Override
		public Long getResult() {
			final Long damageId = getNativeBufferHelper().readUnsignedInt();
			return damageId;
		}

		@Override
		protected boolean callImpl() {
			final boolean error = XExtensionNative.nativeXDamageCreate(
					getConnectionReference().longValue(), getArgs()[0].longValue(),
					getArgs()[1].intValue(), getNativeBufferHelper()
							.getBuffer());
			return error;
		}
	}

	// TODO args doc
	public XDamageSubtract getCallXDamageSubtract() {
		return xDamageSubtract;
	}

	private final XDamageSubtract xDamageSubtract;

	public final class XDamageSubtract extends AbstractXcbCall<Void, Long, Number> {
		private XDamageSubtract() {
		}

		@Override
		protected boolean callImpl() {
			final boolean error = XExtensionNative.nativeXDamageSubtract(
					getConnectionReference().longValue(), getArgs()[0].longValue(),
					getArgs()[1].longValue(), getArgs()[2].longValue(),
					getNativeBufferHelper().getBuffer());
			return error;
		}
	}

	// TODO more

	// ********END XDAMAGE*******//
	// ****START XRENDER****//

	// TODO args doc
	public XRenderInit getCallXRenderInit() {
		return xRenderInit;
	}

	private final XRenderInit xRenderInit;

	public final class XRenderInit extends
			AbstractXcbCall<XcbExtensionVersionReply, Long, Integer> {
		private XRenderInit() {
		}

		@Override
		public XcbExtensionVersionReply getResult() {
			return XExtensionNativeCalls.this
					.readVersionReply(getNativeBufferHelper());
		}

		@Override
		protected boolean callImpl() {
			final int majorVersion = getArgs()[0].intValue();
			final int minorVersion = getArgs()[1].intValue();
			final boolean error = XExtensionNative.nativeXRenderInit(
					getConnectionReference().longValue(), majorVersion, minorVersion,
					getNativeBufferHelper().getBuffer());
			return error;
		}
	}

	// TODO more

	// ********END XRENDER*******//
	// ****START XSHAPE****//

	// TODO args doc
	public XShapeInit getCallXShapeInit() {
		return xShapeInit;
	}

	private final XShapeInit xShapeInit;

	public final class XShapeInit extends
			AbstractXcbCall<XcbExtensionVersionReply, Long, Integer> {
		private XShapeInit() {
		}

		@Override
		public XcbExtensionVersionReply getResult() {
			return XExtensionNativeCalls.this
					.readVersionReply(getNativeBufferHelper());
		}

		@Override
		protected boolean callImpl() {
			final int majorVersion = getArgs()[0].intValue();
			final int minorVersion = getArgs()[1].intValue();
			final boolean error = XExtensionNative.nativeXShapeInit(
					getConnectionReference().longValue(), majorVersion, minorVersion,
					getNativeBufferHelper().getBuffer());
			return error;
		}
	}

	// TODO more

	// *******END XSHAPE*******//
	// ****START XSYNC****//

	// TODO args doc
	public XSyncInit getCallXSyncInit() {
		return xSyncInit;
	}

	private final XSyncInit xSyncInit;

	public final class XSyncInit extends
			AbstractXcbCall<XcbExtensionVersionReply, Long, Integer> {
		private XSyncInit() {
		}

		@Override
		public XcbExtensionVersionReply getResult() {
			return XExtensionNativeCalls.this
					.readVersionReply(getNativeBufferHelper());
		}

		@Override
		protected boolean callImpl() {
			final int majorVersion = getArgs()[0].intValue();
			final int minorVersion = getArgs()[1].intValue();
			final boolean error = XExtensionNative.nativeXSyncInit(
					getConnectionReference().longValue(), majorVersion, minorVersion,
					getNativeBufferHelper().getBuffer());
			return error;
		}
	}

	// TODO args doc
	public XSyncAwaitCondition getCallXSyncAwaitCondition() {
		return xSyncAwaitCondition;
	}

	private final XSyncAwaitCondition xSyncAwaitCondition;

	public final class XSyncAwaitCondition extends
			AbstractXcbCall<Void, Long, Number> {
		private XSyncAwaitCondition() {
		}

		@Override
		protected boolean callImpl() {
			final boolean error = XExtensionNative.nativeXSyncAwaitCondition(
					getConnectionReference().longValue(), getNativeBufferHelper()
							.getBuffer());
			return error;
		}
	}

	// TODO more

	// *******END XSYNC******//
	// ****START XCOMPOSITE****//

	// TODO args doc
	public XCompositeInit getCallXCompositeInit() {
		return xCompositeInit;
	}

	private final XCompositeInit xCompositeInit;

	public final class XCompositeInit extends
			AbstractXcbCall<XcbExtensionVersionReply, Long, Integer> {
		private XCompositeInit() {
		}

		@Override
		public XcbExtensionVersionReply getResult() {
			return XExtensionNativeCalls.this
					.readVersionReply(getNativeBufferHelper());
		}

		@Override
		protected boolean callImpl() {
			final int majorVersion = getArgs()[0].intValue();
			final int minorVersion = getArgs()[1].intValue();
			final boolean error = XExtensionNative.nativeXCompositeInit(
					getConnectionReference().longValue(), majorVersion, minorVersion,
					getNativeBufferHelper().getBuffer());
			return error;
		}
	}

	// TODO args doc
	public XCompositeRedirectSubwindow getCallXCompositeRedirectSubwindow() {
		return xCompositeRedirectSubwindow;
	}

	private final XCompositeRedirectSubwindow xCompositeRedirectSubwindow;

	public final class XCompositeRedirectSubwindow extends
			AbstractXcbCall<Void, Long, Number> {
		private XCompositeRedirectSubwindow() {
		}

		@Override
		protected boolean callImpl() {
			return XExtensionNative.nativeXCompositeRedirectSubwindow(
					getConnectionReference().longValue(), getArgs()[0].longValue(),
					getArgs()[1].intValue(), getNativeBufferHelper()
							.getBuffer());
		}
	}

	// TODO more

	// **********END XCOMPOSITE********//
	// ****START XFIXES****//

	// TODO args doc
	public XFixesInit getCallXFixesInit() {
		return xFixesInit;
	}

	private final XFixesInit xFixesInit;

	public final class XFixesInit extends
			AbstractXcbCall<XcbExtensionVersionReply, Long, Integer> {
		private XFixesInit() {
		}

		@Override
		public XcbExtensionVersionReply getResult() {
			return XExtensionNativeCalls.this
					.readVersionReply(getNativeBufferHelper());
		}

		@Override
		protected boolean callImpl() {
			final int majorVersion = getArgs()[0].intValue();
			final int minorVersion = getArgs()[1].intValue();
			final boolean error = XExtensionNative.nativeXFixesInit(
					getConnectionReference().longValue(), majorVersion, minorVersion,
					getNativeBufferHelper().getBuffer());
			return error;
		}
	}

	// TODO args doc
	public XFixesCreateRegionFromWindow getCallXFixesCreateRegionFromWindow() {
		return xFixesCreateRegionFromWindow;
	}

	private final XFixesCreateRegionFromWindow xFixesCreateRegionFromWindow;

	public final class XFixesCreateRegionFromWindow extends
			AbstractXcbCall<Long, Long, Number> {
		private XFixesCreateRegionFromWindow() {
		}

		@Override
		public Long getResult() {
			final Long regionId = Long.valueOf(getNativeBufferHelper()
					.readUnsignedInt());
			return regionId;
		}

		@Override
		protected boolean callImpl() {
			final long display = getConnectionReference().longValue();
			final long windowId = getArgs()[0].longValue();
			final int regionBounding = getArgs()[1].intValue();
			final boolean error = XExtensionNative
					.nativeXFixesCreateRegionFromWindow(display, windowId,
							regionBounding, getNativeBufferHelper().getBuffer());
			return error;
		}
	}

	// TODO args doc
	public XFixesTranslateRegion getCallXFixesTranslateRegion() {
		return xFixesTranslateRegion;
	}

	private final XFixesTranslateRegion xFixesTranslateRegion;

	public final class XFixesTranslateRegion extends
			AbstractXcbCall<Void, Long, Number> {
		private XFixesTranslateRegion() {
		}

		@Override
		protected boolean callImpl() {
			final long display = getConnectionReference().longValue();
			final long regionId = getArgs()[0].longValue();
			final int x = getArgs()[1].intValue();
			final int y = getArgs()[2].intValue();

			final boolean error = XExtensionNative.nativeXFixesTranslateRegion(
					display, regionId, x, y, getNativeBufferHelper()
							.getBuffer());
			return error;
		}
	}

	// TODO args doc
	public XFixesSetPictureClipRegion getCallXFixesSetPictureClipRegion() {
		return xFixesSetPictureClipRegion;
	}

	private final XFixesSetPictureClipRegion xFixesSetPictureClipRegion;

	public final class XFixesSetPictureClipRegion extends
			AbstractXcbCall<Void, Long, Number> {
		private XFixesSetPictureClipRegion() {
		}

		@Override
		protected boolean callImpl() {
			final long display = getConnectionReference().longValue();
			final long pictureId = getArgs()[0].longValue();
			final int clipXOrigin = getArgs()[1].intValue();
			final int clipYOrigin = getArgs()[2].intValue();
			final long regionId = getArgs()[3].longValue();

			final boolean error = XExtensionNative
					.nativeXFixesSetPictureClipRegion(display, pictureId,
							clipXOrigin, clipYOrigin, regionId,
							getNativeBufferHelper().getBuffer());
			return error;
		}
	}

	// TODO args doc
	public XFixesDestroyRegion getCallXFixesDestroyRegion() {
		return xFixesDestroyRegion;
	}

	private final XFixesDestroyRegion xFixesDestroyRegion;

	public final class XFixesDestroyRegion extends
			AbstractXcbCall<Void, Long, Long> {
		private XFixesDestroyRegion() {
		}

		@Override
		protected boolean callImpl() {
			final long display = getConnectionReference().longValue();
			final long regionId = getArgs()[0].longValue();

			final boolean error = XExtensionNative.nativeXFixesDestroyRegion(
					display, regionId, getNativeBufferHelper().getBuffer());
			return error;
		}
	}

	// *******END XFIXES*******//

	public XExtensionNativeCalls() {
		// ****XDAMAGE****//
		this.xDamageSubtract = new XDamageSubtract();
		this.xDamageCreate = new XDamageCreate();
		this.xDamageInit = new XDamageInit();
		// ***************//
		// ****XRENDER****//
		this.xRenderInit = new XRenderInit();
		// ***************//
		// ****XSHAPE****//
		this.xShapeInit = new XShapeInit();
		// **************//
		// ****XFIXES****//
		this.xFixesInit = new XFixesInit();
		this.xFixesCreateRegionFromWindow = new XFixesCreateRegionFromWindow();
		this.xFixesTranslateRegion = new XFixesTranslateRegion();
		this.xFixesSetPictureClipRegion = new XFixesSetPictureClipRegion();
		this.xFixesDestroyRegion = new XFixesDestroyRegion();
		// **************//
		// ****XCOMPOSITE****//
		this.xCompositeRedirectSubwindow = new XCompositeRedirectSubwindow();
		this.xCompositeInit = new XCompositeInit();
		// ******************//
		// ****XSYNC****//
		this.xSyncInit = new XSyncInit();
		this.xSyncAwaitCondition = new XSyncAwaitCondition();
		// *************//
	}

	private XcbExtensionVersionReply readVersionReply(
			final NativeBufferHelper nativeBufferHelper) {
		final int majorVersion = (int) nativeBufferHelper.readUnsignedInt();
		final int minorVersion = (int) nativeBufferHelper.readUnsignedInt();

		final XcbExtensionVersionReply reply = new XcbExtensionVersionReply(
				majorVersion, minorVersion);
		return reply;
	}
}
