package org.hyperdrive.widget;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class Label extends Widget {

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	@ViewDefinition
	public interface View extends Widget.View {
		PaintInstruction<Void> onTextUpdate(String name, Object... args);
	}

	@Override
	public View getView() {
		return (View) super.getView();
	}
}
