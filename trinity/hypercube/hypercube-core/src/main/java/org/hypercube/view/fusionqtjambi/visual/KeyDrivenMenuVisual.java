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
package org.hypercube.view.fusionqtjambi.visual;

import java.util.List;

import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.core.Qt.TextFormat;
import com.trolltech.qt.core.Qt.TextInteractionFlag;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QResizeEvent;
import com.trolltech.qt.gui.QWidget;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class KeyDrivenMenuVisual extends QFrame {

	private static final String NAME = "KeyDrivenMenu";
	private static final String DESIRED_CHOICE_MARKUP = "<span style=\"background-color: orange;\">%s</span>";
	private static final String EMPTY_LIST_MESSAGE = "No choice specified.";
	private static final String INPUT_LABEL_NAME = "KeyDrivenMenu-input";
	private static final String CHOICES_LABEL_NAME = "KeyDrivenMenu-choices";

	private final QLabel input;
	private final QLabel choices;

	private static final int INPUT_WIDTH = 150;

	/**
	 * 
	 */
	public KeyDrivenMenuVisual(final QWidget parentPaintPeer) {
		super(parentPaintPeer, WindowType.X11BypassWindowManagerHint);
		setFocusPolicy(FocusPolicy.NoFocus);
		setLineWidth(1);
		setObjectName(getStyleName());

		this.input = new QLabel(this);
		this.input.setObjectName(KeyDrivenMenuVisual.INPUT_LABEL_NAME);
		this.input
				.setTextInteractionFlags(TextInteractionFlag.NoTextInteraction);
		this.input.setTextFormat(TextFormat.RichText);
		this.input.show();

		this.choices = new QLabel(this);
		this.choices.setObjectName(KeyDrivenMenuVisual.CHOICES_LABEL_NAME);
		this.choices
				.setTextInteractionFlags(TextInteractionFlag.NoTextInteraction);
		this.choices.setTextFormat(TextFormat.RichText);
		this.choices.show();
	}

	@Override
	protected void resizeEvent(final QResizeEvent resizeEvent) {
		super.resizeEvent(resizeEvent);
		final int width = resizeEvent.size().width();
		final int height = resizeEvent.size().height();

		if (width >= height) {
			// horizontal layout
			this.input.resize(KeyDrivenMenuVisual.INPUT_WIDTH, height);
			this.choices.setGeometry(KeyDrivenMenuVisual.INPUT_WIDTH, 0, width
					- KeyDrivenMenuVisual.INPUT_WIDTH, height);

		} else {
			// vertical layout
		}
	}

	/**
	 * 
	 */
	public void clearVisual() {
		this.input.clear();
		this.choices.clear();
	}

	/**
	 * 
	 * @param input
	 * @param possibleValues
	 * @param activeValue
	 */
	public void updateVisual(final String input,
			final List<String> possibleValues, final int activeValue) {

		if (input.isEmpty()) {
			this.input.setText(KeyDrivenMenuVisual.EMPTY_LIST_MESSAGE);
			this.choices.clear();
		} else if (possibleValues.isEmpty()) {
			this.input.setText(input);
		} else {
			this.input.setText(input);

			final StringBuffer possibleValuesConcatinator = new StringBuffer(
					100);
			int i = 0;
			for (final String possibleValue : possibleValues) {
				if (i == activeValue) {
					possibleValuesConcatinator.append(String.format(
							KeyDrivenMenuVisual.DESIRED_CHOICE_MARKUP,
							possibleValue));
				} else {
					possibleValuesConcatinator.append(possibleValue);
				}
				possibleValuesConcatinator.append(' ');
				i++;
			}
			this.choices.setText(possibleValuesConcatinator.toString());
		}
	}

	public String getStyleName() {
		return KeyDrivenMenuVisual.NAME;
	}

	/**
	 * 
	 */
	private void forceStyleUpdate() {
		style().unpolish(this);
		style().polish(this);
	}

	/**
	 * 
	 */
	public void activate() {
		setProperty("active", true);
		forceStyleUpdate();
	}

	/**
	 * 
	 */
	public void deactivate() {
		setProperty("active", false);
		forceStyleUpdate();
	}
}
