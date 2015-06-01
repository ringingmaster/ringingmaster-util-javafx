package com.concurrentperformance.fxutils.propertyeditor;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
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
public class LabelPropertyValue extends SkeletalPropertyValue implements PropertyValue {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public LabelPropertyValue(String propertyName) {
		super(propertyName);
		setEditor(buildLabel());
	}

	private Label buildLabel() {
		Label textField = new Label();
		textField.setPadding(new Insets(0,2,0,2));
		textField.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		//TODO textField.setPromptText("TEST");

		return textField;
	}

	public void setValue(String value) {
		final Label textField = (Label) getEditor();
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
		((Label)getEditor()).setFont(font);
	}

	@Override
	public String toString() {
		return "DisplayPropertyValue{" +
				"name=" + getName() +
				'}';
	}
}
