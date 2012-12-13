package org.trinity.render.qt.impl.painter.instructions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.render.api.PaintRoutine;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.render.qt.api.QJPaintContext;
import org.trinity.shellplugin.widget.api.binding.ViewProperty;
import org.trinity.shellplugin.widget.api.binding.ViewPropertyDiscovery;
import org.trinity.shellplugin.widget.api.binding.ViewSlotInvocationHandler;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.io.CharStreams;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QGraphicsDropShadowEffect;
import com.trolltech.qt.gui.QWidget;

public class QJCreateSurfaceRoutine implements PaintRoutine<Void, QJPaintContext> {

	private final ViewPropertyDiscovery viewPropertyDiscovery;
	private final ViewSlotInvocationHandler viewSlotInvocationHandler;

	private final Optional<QWidget> parentView;
	private final QWidget view;

	public QJCreateSurfaceRoutine(	ViewPropertyDiscovery viewPropertyDiscovery,
									ViewSlotInvocationHandler viewSlotInvocationHandler,
									Optional<QWidget> parentView,
									QWidget view) {
		this.viewPropertyDiscovery = viewPropertyDiscovery;
		this.viewSlotInvocationHandler = viewSlotInvocationHandler;

		this.parentView = parentView;

		Preconditions.checkArgument(view instanceof QWidget,
									"Expected view to be of type " + QWidget.class.getName());

		this.view = view;
	}

	@Override
	public Void call(QJPaintContext paintContext) {

		createDisplaySurface(paintContext);

		PaintableSurfaceNode paintableSurfaceNode = paintContext.getPaintableSurfaceNode();

		initializeAllViewProperties(viewPropertyDiscovery,
									viewSlotInvocationHandler,
									paintableSurfaceNode,
									view);

		setStyle(view);

		return null;
	}

	private void createDisplaySurface(final QJPaintContext paintContext) {
		if (parentView.isPresent()) {
			view.setParent(parentView.get());
		}

		final QGraphicsDropShadowEffect effect = new QGraphicsDropShadowEffect();
		effect.setBlurRadius(10);
		effect.setOffset(	0,
							5);
		effect.setColor(QColor.darkGray);
		view.setGraphicsEffect(effect);

		view.setWindowFlags(WindowType.X11BypassWindowManagerHint);
		view.setAttribute(	WidgetAttribute.WA_DeleteOnClose,
							true);
		view.setAttribute(	WidgetAttribute.WA_DontCreateNativeAncestors,
							true);

		paintContext.syncVisualGeometryToSurfaceNode(view);

	}

	private void initializeAllViewProperties(	ViewPropertyDiscovery viewPropertyDiscovery,
												ViewSlotInvocationHandler viewSlotInvocationHandler,
												PaintableSurfaceNode paintableSurfaceNode,
												Object view) {
		Method[] fields;

		try {
			fields = viewPropertyDiscovery.lookupAllViewProperties(paintableSurfaceNode.getClass());
			for (final Method method : fields) {
				final ViewProperty viewProperty = method.getAnnotation(ViewProperty.class);
				final Optional<Method> viewSlot = viewPropertyDiscovery.lookupViewSlot(	view.getClass(),
																						viewProperty.value());
				if (!viewSlot.isPresent()) {
					continue;
				}

				final Object argument = method.invoke(paintableSurfaceNode);
				viewSlotInvocationHandler.invokeSlot(	paintableSurfaceNode,
														viewProperty,
														view,
														viewSlot.get(),
														argument);
			}
		} catch (ExecutionException e) {
			Throwables.propagate(e);
		} catch (IllegalAccessException e) {
			Throwables.propagate(e);
		} catch (IllegalArgumentException e) {
			Throwables.propagate(e);
		} catch (InvocationTargetException e) {
			Throwables.propagate(e);
		}
	}

	private void setStyle(QWidget view) {
		if (view == null) {
			return;
		}
		try {
			view.setStyleSheet(getStyleSheet());
			view.ensurePolished();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getStyleSheet() throws IOException {
		final Class<? extends QWidget> viewClass = view.getClass();
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
}