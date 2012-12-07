/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.render.qt.lnf.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.render.qt.api.QJPaintContext;
import org.trinity.shell.api.widget.ShellWidget;
import org.trinity.shell.api.widget.ShellWidgetView;
import org.trinity.shellplugin.widget.api.binding.ViewProperty;
import org.trinity.shellplugin.widget.api.binding.ViewPropertyDiscovery;
import org.trinity.shellplugin.widget.api.binding.ViewPropertySlot;
import org.trinity.shellplugin.widget.api.binding.ViewSlotInvocationHandler;

import com.google.common.base.Optional;
import com.google.common.io.CharStreams;
import com.google.inject.Inject;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QGraphicsDropShadowEffect;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
public class BaseShellWidgetView implements ShellWidgetView {

	private Painter painter;

	@Inject
	private ViewSlotInvocationHandler viewSlotInvocationHandler;

	@Inject
	private ViewPropertyDiscovery viewPropertyDiscovery;

	protected Painter getPainter() {
		return this.painter;
	}

	protected <R> Future<R> invokePaintInstruction(final PaintInstruction<R, QJPaintContext> paintInstruction) {
		final Painter painter = getPainter();
		if (this.painter == null) {
			throw new IllegalStateException("Display surface not created!");
		}
		return painter.instruct(paintInstruction);
	}

	@Override
	public final Future<Void> createDisplaySurface(	final Painter painter,
													final ShellWidget parent) {
		if (this.painter == null) {
			this.painter = painter;
		} else {
			throw new IllegalStateException("Display surface already created!");
		}

		return invokePaintInstruction(new PaintInstruction<Void, QJPaintContext>() {
			@Override
			public Void call(final QJPaintContext paintContext) {
				try {
					createDisplaySurfaceInstruction(paintContext,
													parent);
					setAllViewProperties(paintContext);
					setStyle(paintContext);
				} catch (final Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}

	protected void setStyle(final QJPaintContext paintContext) {
		final QWidget visual = paintContext.getVisual(paintContext.getPaintableSurfaceNode());
		if (visual == null) {
			return;
		}
		try {
			visual.setStyleSheet(getStyleSheet());
			visual.ensurePolished();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void createDisplaySurfaceInstruction(	final QJPaintContext paintContext,
													final ShellWidget parent) {
		QWidget parentVisual = null;
		if (parent != null) {
			parentVisual = paintContext.getVisual(parent);
		}
		final QWidget visual = createVisual(parentVisual);

		visual.setWindowFlags(WindowType.X11BypassWindowManagerHint);
		visual.setAttribute(WidgetAttribute.WA_DeleteOnClose,
							true);
		visual.setAttribute(WidgetAttribute.WA_DontCreateNativeAncestors,
							true);

		paintContext.syncVisualGeometryToSurfaceNode(visual);
		paintContext.setVisual(visual);
	}

	protected void setAllViewProperties(final QJPaintContext paintContext) {
		final PaintableSurfaceNode paintableSurfaceNode = paintContext.getPaintableSurfaceNode();

		Method[] fields;
		try {
			fields = this.viewPropertyDiscovery.lookupAllViewProperties(paintableSurfaceNode.getClass());

			for (final Method method : fields) {
				final ViewProperty viewProperty = method.getAnnotation(ViewProperty.class);
				final Optional<Method> viewSlot = this.viewPropertyDiscovery.lookupViewSlot(getClass(),
																							viewProperty.value());
				if (!viewSlot.isPresent()) {
					continue;
				}

				final Object argument = method.invoke(paintableSurfaceNode);
				this.viewSlotInvocationHandler.invokeSlot(	paintableSurfaceNode,
															viewProperty,
															this,
															viewSlot.get(),
															argument);
			}
		} catch (final ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected QWidget createVisual(final QWidget parentVisual) {
		final QFrame visual = new QFrame(parentVisual);
		final QGraphicsDropShadowEffect effect = new QGraphicsDropShadowEffect();
		effect.setBlurRadius(10);
		effect.setOffset(	0,
							5);
		effect.setColor(QColor.darkGray);

		visual.setGraphicsEffect(effect);
		return visual;
	}

	protected String getStyleSheet() throws IOException {
		final Class<? extends BaseShellWidgetView> viewClass = getClass();
		final String className = viewClass.getName();
		final InputStream in = viewClass.getClassLoader().getResourceAsStream(className + ".qss");
		final InputStreamReader inReader = new InputStreamReader(	in,
																	"UTF-8");
		String styleSheet = "";
		try {
			styleSheet = CharStreams.toString(inReader);
		} finally {
			inReader.close();
		}
		return styleSheet;
	}

	@Override
	public final Future<Void> destroy() {
		return invokePaintInstruction(new PaintInstruction<Void, QJPaintContext>() {
			@Override
			public Void call(final QJPaintContext paintContext) {
				destroyInstruction(paintContext);
				return null;
			}
		});
	}

	protected void destroyInstruction(final QJPaintContext paintContext) {
		final QWidget visual = paintContext.getVisual(paintContext.getPaintableSurfaceNode());
		visual.close();
		paintContext.disposeVisual();
	}

	@Override
	public final Future<DisplaySurface> getDislaySurface() {
		final Painter painter = getPainter();
		if (this.painter == null) {
			return null;
		}
		return painter.instruct(new PaintInstruction<DisplaySurface, QJPaintContext>() {
			@Override
			public DisplaySurface call(final QJPaintContext paintContext) {
				return getDisplaySurfaceInstruction(paintContext);
			}
		});
	}

	protected DisplaySurface getDisplaySurfaceInstruction(final QJPaintContext paintContext) {
		final QWidget visual = paintContext.getVisual(paintContext.getPaintableSurfaceNode());
		final DisplaySurface displaySurface = paintContext.getDisplaySurface(visual);
		return displaySurface;
	}

	@ViewPropertySlot("objectName")
	public void setObjectName(	final QJPaintContext paintContext,
								final String name) {
		final QWidget visual = paintContext.getVisual(paintContext.getPaintableSurfaceNode());
		if (visual != null) {
			visual.setObjectName(name);
			refreshVisual(visual);
		}
	}

	private void refreshVisual(final QWidget visual) {
		visual.style().unpolish(visual);
		visual.ensurePolished();
	}
}