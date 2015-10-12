package com.concurrentperformance.fxutils.components;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;

/**
 * A simple override of a button to allow the protected final setPressed() to be accessed.
 *
 * @author Lake
 */
public class PressableButton extends Button {

	private final SimpleBooleanProperty pressedProperty = new SimpleBooleanProperty(true);

//	public BooleanProperty pressedOverrideProperty() {
//		return pressedProperty();
//	}


	public void setPressedOverride(boolean pressedOverride) {
		setPressed(pressedOverride);
	}
}
