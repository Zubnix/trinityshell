package org.hyperdrive.api.widget;

public interface Label extends Widget {

	public interface View extends Widget.View {
		PaintInstruction<Void> labelUpdated(String labelValue);
	}

	@Override
	public View getView();

	void updateLabel(final String name);
}
