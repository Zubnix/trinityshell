package org.hyperdrive.widget;

import org.hyperdrive.api.widget.PaintInstruction;
import org.hyperdrive.api.widget.ViewDefinition;


//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class Label extends BaseWidget {

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	@ViewDefinition
	public interface View extends BaseWidget.View {
		PaintInstruction<Void> onTextUpdate(String name, Object... args);
	}

	@Override
	public View getView() {
		return (View) super.getView();
	}
}
