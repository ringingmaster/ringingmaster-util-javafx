package com.concurrentperformance.fxutils.propertyeditor;

import javafx.beans.value.ChangeListener;
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
public class TextPropertyValue extends SkeletalPropertyValue implements PropertyValue {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public TextPropertyValue(String propertyName, ChangeListener<String> listener) {
		super(propertyName);
		setEditor(buildTextField(listener));
	}

	private TextField buildTextField(ChangeListener<String> listener) {
		TextField textField = new TextField();
		textField.setPadding(new Insets(0,2,0,2));
		textField.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		textField.textProperty().addListener(listener);
		//TODO textField.setPromptText("TEST");

		return textField;
	}

	public void setValue(String value) {
		final TextField editor = (TextField) getEditor();
		if (value == null ||
			!editor.getText().equals(value)) {
			editor.setText(value);
		}
	}

	@Override
	public void setFont(Font font) {
		((TextField)getEditor()).setFont(font);
	}
}
