package com.concurrentperformance.fxutils.propertyeditor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

	public TextPropertyValue(String propertyName) {
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

	public void setListener(ChangeListener<String> listener, boolean everyKeyPress) {
		TextField textField = (TextField) getEditor();
		if (everyKeyPress) {
			textField.textProperty().addListener(listener);
		}
		else {
			// just loss of focus.
			textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
					if (newValue == false) {
						notifyListener(listener, textField);
					}
				}
			});

			textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					if (event.getCode().equals(KeyCode.ENTER))
						notifyListener(listener, textField);
				}
			});
		}
	}

	static String previousNotifiedValue = null;

	private void notifyListener(ChangeListener<String> listener, TextField textField) {

		final String newValue = textField.textProperty().getValue();

		if (newValue != null && !newValue.equals(previousNotifiedValue)) {
			listener.changed(textField.textProperty(), previousNotifiedValue, newValue);
			previousNotifiedValue = newValue;
		}
	}


	public void setValue(String value) {
		final TextField textField = (TextField) getEditor();
		if (value == null ||
			!textField.getText().equals(value)) {
			textField.setText(value);
		}
	}

	@Override
	public void setFont(Font font) {
		((TextField)getEditor()).setFont(font);
	}
}
