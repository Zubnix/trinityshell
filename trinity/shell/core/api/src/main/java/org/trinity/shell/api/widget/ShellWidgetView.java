// /*
// * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
// * This program is free software: you can redistribute it and/or modify it
// under
// * the terms of the GNU General Public License as published by the Free
// Software
// * Foundation, either version 3 of the License, or (at your option) any later
// * version. This program is distributed in the hope that it will be useful,
// but
// * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
// or
// * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more
// * details. You should have received a copy of the GNU General Public License
// * along with this program. If not, see <http://www.gnu.org/licenses/>.
// */
// package org.trinity.shell.api.widget;
//
// import java.util.concurrent.Future;
//
// import org.trinity.foundation.display.api.DisplaySurface;
// import org.trinity.foundation.render.api.Painter;
//
// import com.google.common.base.Optional;
//
// /**
// *
// * Paint delegate for a {@link ShellWidget}.
// *
// */
// public interface ShellWidgetView {
// /**
// * Create a {@link DisplaySurface} for this view's {@link ShellWidget}.
// * <p>
// * Calling this method multiple times can have undesired effects and is
// * implementation dependent. As a general rule this should be avoided.
// *
// * @param painter
// * The {@link Painter} of the {@code ShellWidget}
// * @param closestParentWidget
// * The closest parent that is of type {@link ShellWidget}.
// * @return A {@link Future} who's {@link Future#get()} method will block
// * until the {@code DisplaySurface} has been created.
// */
// Optional<Future<Void>> createDisplaySurface(Painter painter,
// ShellWidget closestParentWidget);
//
// /**
// *
// * The created {@link DisplaySurface} of this view's {@link ShellWidget}.
// *
// * @return A {@link DisplaySurface}.
// */
// Optional<Future<DisplaySurface>> getDislaySurface();
//
// /**
// * Destroy any render back-end related resources of this view's
// * {@link ShellWidget}.
// *
// * @return A {@link Future} who's {@link Future#get()} method will block
// * until the destroy operation has been completed on the render
// * back-end.
// */
// Optional<Future<Void>> destroyView();
// }