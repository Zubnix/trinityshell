/*
 * This file is part of Hypercube.
 * 
 * Hypercube is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hypercube is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hypercube. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hypercube.view.fusionqtjambi;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.fusion.qt.painter.QFusionPaintCallBack;
import org.hypercube.error.InaccessibleWidgetViewClass;
import org.hyperdrive.widget.ClientManager.ClientManagerLabel.ClientManagerLabelView;
import org.hyperdrive.widget.KeyDrivenMenu.KeyDrivenMenuView;
import org.hyperdrive.widget.View;
import org.hyperdrive.widget.ViewFactory;
import org.hyperdrive.widget.Widget;

import com.trolltech.qt.gui.QWidget;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class QFusionViewFactoryImpl implements
		ViewFactory<QFusionPaintCallBack<? extends QWidget, ?>> {

	private static final Logger logger = Logger
			.getLogger(QFusionViewFactoryImpl.class);
	private static final String VIEW_INSTANTIATION_LOGMESSAGE = "Could not intstantiate new view object.";

	private final Map<Object, Class<? extends View>> viewMap;

	/**
	 * 
	 */
	public QFusionViewFactoryImpl() {
		this.viewMap = new HashMap<Object, Class<? extends View>>();
	}

	@Override
	public void registerCustomView(final Widget widget,
			final Class<? extends View> view) {
		this.viewMap.put(widget, view);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends View> T newCustomView(final Widget widget) {
		try {
			return (T) this.viewMap.get(widget).newInstance();
		} catch (InstantiationException e) {

			InaccessibleWidgetViewClass ex = new InaccessibleWidgetViewClass(
					this.viewMap.get(widget), e);

			logger.error(VIEW_INSTANTIATION_LOGMESSAGE, ex);

			throw ex;
		} catch (IllegalAccessException e) {

			InaccessibleWidgetViewClass ex = new InaccessibleWidgetViewClass(
					this.viewMap.get(widget), e);

			logger.error(VIEW_INSTANTIATION_LOGMESSAGE, ex);

			throw ex;
		}
	}

	@Override
	public CloseButtonViewImpl newCloseButtonView() {
		return new CloseButtonViewImpl();
	}

	@Override
	public DragButtonViewImpl newDragButtonView() {
		return new DragButtonViewImpl();
	}

	@Override
	public RealRootViewImpl newRealRootView() {
		return new RealRootViewImpl();
	}

	@Override
	public ResizeButtonViewImpl newResizeButtonView() {
		return new ResizeButtonViewImpl();
	}

	@Override
	public VirtRootViewImpl newVirtualRootView() {
		return new VirtRootViewImpl();
	}

	@Override
	public org.hyperdrive.widget.ClientNameLabel.ClientNameLabelView newClientNameLabelView() {
		return new ClientNameLabelViewImpl();
	}

	@Override
	public KeyDrivenMenuView newKeyDrivenMenuView() {
		return new KeyDrivenMenuViewImpl();
	}

	@Override
	public ClientManagerViewImpl newClientManagerView() {
		return new ClientManagerViewImpl();
	}

	@Override
	public WidgetViewImpl newBaseView() {
		return new WidgetViewImpl();
	}

	@Override
	public MaximizeButtonViewImpl newMaximizeButtonView() {
		return new MaximizeButtonViewImpl();
	}

	@Override
	public ClientManagerLabelView newClientManagerLabelView() {
		return new ClientManagerLabelViewImpl();
	}

	@Override
	public HideButtonViewImpl newHideButtonView() {
		return new HideButtonViewImpl();
	}
}