package com.concurrentperformance.fxutils.components;

import javafx.scene.control.Button;

/**
 * A simple override of a button to allow the protected final setPressed() to be accessed.
 *
 * @author Lake
 */
public class PressableButton extends Button {

	public void setPressedState(boolean pressed) {
		setPressed(pressed);
	}
}
