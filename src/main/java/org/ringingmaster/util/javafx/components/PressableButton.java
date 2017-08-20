package org.ringingmaster.util.javafx.components;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;

/**
 * A simple override of a button to allow the protected final setPressed() to be accessed through a property.
 *
 * @author Lake
 */
public class PressableButton extends Button {

	private final SimpleBooleanProperty pressedOverrideProperty = new SimpleBooleanProperty(true);

	{
		pressedOverrideProperty.addListener((observable, oldValue, newValue) -> setPressed(newValue));
	}

	public PressableButton() {
		super();
	}

	public PressableButton(String text, Node graphic) {
		super(text, graphic);

	}

	public BooleanProperty pressedOverrideProperty() {
		return pressedOverrideProperty;
	}

	public void setPressedOverride(boolean pressedOverride) {
		setPressed(pressedOverride);
	}
}
