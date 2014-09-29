package com.concurrentperformance.fxutils.propertyeditor;

import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * TODO comments ???
 *
 * @author Lake
 */
public class TextPropertyValue extends SkeletalPropertyValue implements PropertyValue {

	public TextPropertyValue(String propertyName, String propertyValue) {
		super(propertyName, buildTextField(propertyValue));
	}

	private static Region buildTextField(String propertyValue) {
		TextField textField = new TextField(propertyValue);
		textField.setPadding(new Insets(0,2,0,2));
		textField.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

		return textField;
	}

	@Override
	public void setFont(Font font) {
		((TextField)getEditor()).setFont(font);
	}
}
