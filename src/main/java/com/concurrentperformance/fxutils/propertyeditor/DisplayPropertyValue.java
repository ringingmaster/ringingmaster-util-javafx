package com.concurrentperformance.fxutils.propertyeditor;

import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO comments ???
 *
 * @author Lake
 */
public class DisplayPropertyValue extends SkeletalPropertyValue implements PropertyValue {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public DisplayPropertyValue(String propertyName) {
		super(propertyName);
		setEditor(buildTextField());
	}

	private TextField buildTextField() {
		TextField textField = new TextField();
		textField.setPadding(new Insets(0,2,0,2));
		textField.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		//TODO textField.setPromptText("TEST");

		return textField;
	}

	public void setValue(String value) {
		final TextField textField = (TextField) getEditor();
		if (value == null ||
			!textField.getText().equals(value)) {
			textField.setText(value);
		}
	}

	public void setColor(Color color) {
		getEditor().setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
	}

	@Override
	public void setFont(Font font) {
		((TextField)getEditor()).setFont(font);
	}

	@Override
	public String toString() {
		return "DisplayPropertyValue{" +
				"name=" + getName() +
				'}';
	}
}
