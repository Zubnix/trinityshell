package org.trinity.shellplugin.wm.view.javafx.skin;

import javafx.scene.text.Text;

public class Icon extends Text {

    public Icon() {
    }

    public Icon(Icon icon) {
        super(icon.getText());
        setStyle(icon.getStyle());
        getStyleClass().addAll(icon.getStyleClass());
    }
}
