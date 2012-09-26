package org.trinity.render.swt.api;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;

final class VisualBodyLayout extends Layout {

	private final Control body;

	/**
	 * Constructs a new instance of this class.
	 */
	VisualBodyLayout(Control body) {
		this.body = body;
	}

	protected Point computeSize(Composite composite,
								int wHint,
								int hHint,
								boolean flushCache) {

		if (this.body.getParent() != composite) {
			throw new IllegalStateException("body parent must be same as this layout's composite.");
		}

		int maxWidth = 0, maxHeight = 0;
		int w = wHint, h = hHint;
		if (wHint != SWT.DEFAULT) {
			w = Math.max(	0,
							(wHint));
		}
		Point size = computeChildSize(	body,
										w,
										h,
										flushCache);
		maxWidth = Math.max(maxWidth,
							size.x);
		maxHeight = Math.max(	maxHeight,
								size.y);

		int width = 0, height = 0;
		width = maxWidth;
		height = maxHeight;

		if (wHint != SWT.DEFAULT)
			width = wHint;
		if (hHint != SWT.DEFAULT)
			height = hHint;
		return new Point(	width,
							height);
	}

	Point computeChildSize(	Control control,
							int wHint,
							int hHint,
							boolean flushCache) {
		VisualBodyLayoutData data = (VisualBodyLayoutData) control.getLayoutData();
		if (data == null) {
			data = new VisualBodyLayoutData();
			control.setLayoutData(data);
		}
		Point size = null;
		if (wHint == SWT.DEFAULT && hHint == SWT.DEFAULT) {
			size = data.computeSize(control,
									wHint,
									hHint,
									flushCache);
		} else {
			// TEMPORARY CODE
			int trimX, trimY;
			if (control instanceof Scrollable) {
				Rectangle rect = ((Scrollable) control).computeTrim(0,
																	0,
																	0,
																	0);
				trimX = rect.width;
				trimY = rect.height;
			} else {
				trimX = trimY = control.getBorderWidth() * 2;
			}
			int w = wHint == SWT.DEFAULT ? wHint : Math.max(0,
															wHint - trimX);
			int h = hHint == SWT.DEFAULT ? hHint : Math.max(0,
															hHint - trimY);
			size = data.computeSize(control,
									w,
									h,
									flushCache);
		}
		return size;
	}

	protected boolean flushCache(Control control) {
		Object data = control.getLayoutData();
		if (data != null)
			((VisualBodyLayoutData) data).flushCache();
		return true;
	}

	String getName() {

		String string = getClass().getName();
		int index = string.lastIndexOf('.');
		if (index == -1)
			return string;
		return string.substring(index + 1,
								string.length());
	}

	protected void layout(	Composite composite,
							boolean flushCache) {
		if (this.body.getParent() != composite) {
			throw new IllegalStateException("body parent must be same as this layout's composite.");
		}

		Rectangle rect = composite.getClientArea();

		int width = rect.width;
		int height = rect.height;

		int x = rect.x;
		int y = rect.y, cellWidth = width;
		int childWidth = cellWidth;
		body.setBounds(	x,
						y,
						childWidth,
						height);
	}

	/**
	 * Returns a string containing a concise, human-readable description of the
	 * receiver.
	 * 
	 * @return a string representation of the layout
	 */
	public String toString() {
		String string = getName() + " {";
		string += "type=SWT.VERTICAL";
		string = string.trim();
		string += "}";
		return string;
	}
}
