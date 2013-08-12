/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/

package org.trinity.shellplugin.widget.impl.view.qt;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import org.apache.onami.autobind.annotations.Bind;
import org.trinity.foundation.api.render.binding.view.delegate.Signal;
import org.trinity.foundation.api.render.binding.view.EventSignalFilter;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QMouseEvent;

@Bind
public class LMBSignalFilter extends EventSignalFilter {

    LMBSignalFilter() {
    }

    @Override
    public void installFilter(@Nonnull final Object view,
                              @Nonnull final Signal signal) {
        checkNotNull(view);
        checkNotNull(signal);

        checkArgument(view instanceof QObject,
                "Can only install filter on views of type " + QObject.class.getName());

        QApplication.invokeLater(new Runnable() {
            @Override
            public void run() {
                final QObject qView = (QObject) view;
                qView.installEventFilter(new QObject() {
                    @Override
                    public boolean eventFilter(final QObject watched,
                                               final QEvent event) {
                        if (event.type().equals(QEvent.Type.MouseButtonPress)) {
                            final QMouseEvent mouseEvent = (QMouseEvent) event;
                            if (mouseEvent.button().equals(Qt.MouseButton.LeftButton)) {
                                signal.fire();
                                return true;
                            }
                        }
                        return false;
                    }
                });
            }
        });
    }
}
